package com.stock.webpage.service.impl;

import com.stock.webpage.common.utils.MarketIndicatorProvider;
import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.MyEtfItemHistoryMapper;
import com.stock.webpage.mapper.MyEtfItemMapper;
import com.stock.webpage.service.MyEtfService;
import com.stock.webpage.service.StockViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class MyEtfServiceImpl implements MyEtfService {

    private final MyEtfItemMapper etfItemMapper;
    private final MyEtfItemHistoryMapper historyMapper;
    private final StockViewService stockViewService;
    private final MarketIndicatorProvider marketIndicatorProvider;

    // =========================
    // ETF 목록 요약
    // =========================
    @Override
    public PageResponseDTO<MyEtfSummaryDTO> getMyEtfList(
            String userId,
            PageRequestDTO pageRequestDTO
    ) {

        // 1️⃣ 전체 ETF 개수 (페이징용)
        int total = etfItemMapper.countMyEtf(userId);

        // 2️⃣ 페이징된 ETF 요약 목록
        List<Map<String, Object>> rows =
                etfItemMapper.selectMyEtfSummaryPaging(userId, pageRequestDTO);

        double usdRate = marketIndicatorProvider.getUsdRate();
        List<MyEtfSummaryDTO> dtoList = new ArrayList<>();

        // 3️⃣ 기존 계산 로직 그대로 유지
        for (Map<String, Object> r : rows) {

            String etfName = (String) r.get("etfName");
            String etfDescription = (String) r.get("etfDescription");
            long itemCount = ((Number) r.get("itemCount")).longValue();
            double investedAmount = ((Number) r.get("investedAmount")).doubleValue();

            double evaluatedAmount = 0.0;

            List<MyEtfItemDTO> items =
                    etfItemMapper.selectEtfItemList(userId, etfName, "N");

            for (MyEtfItemDTO item : items) {

                boolean isUsStock =
                        item.getCode() != null &&
                                item.getCode().matches(".*[A-Za-z].*");

                StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
                if (stock == null || stock.getPriceList().isEmpty()) continue;

                double price = stock.getPriceList().get(0).getClose();
                if (isUsStock) price *= usdRate;

                evaluatedAmount += price * item.getQuantity();
            }

            double profitRate =
                    investedAmount > 0
                            ? ((evaluatedAmount - investedAmount) * 100.0) / investedAmount
                            : 0.0;

            dtoList.add(
                    MyEtfSummaryDTO.builder()
                            .etfName(etfName)
                            .etfDescription(etfDescription)
                            .itemCount(itemCount)
                            .investedAmount(investedAmount)
                            .evaluatedAmount(evaluatedAmount)
                            .profitRate(profitRate)
                            .build()
            );
        }

        // 4️⃣ PageResponseDTO로 감싸서 반환
        return PageResponseDTO.<MyEtfSummaryDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // =========================
    // ETF 생성
    // =========================
    @Override
    public void createEtf(String userId, MyEtfCreateRequestDTO request) {

        // ✅ 1. ETF 이름 중복 체크
        int count = etfItemMapper.countByUserIdAndEtfName(
                userId,
                request.getEtfName()
        );

        if (count > 0) {
            throw new IllegalStateException("이미 존재하는 ETF 이름입니다.");
        }

        // ✅ 2. 정상 생성
        for (MyEtfItemRequestDTO item : request.getItems()) {

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            Double priceAtAdd = null;

            if (stock != null && !stock.getPriceList().isEmpty()) {
                priceAtAdd = stock.getPriceList().get(0).getClose();
            }

            etfItemMapper.insertEtfItem(
                    MyEtfItemDTO.builder()
                            .userId(userId)
                            .code(item.getCode())
                            .name(item.getName())
                            .etfName(request.getEtfName())
                            .etfDescription(request.getEtfDescription())
                            .quantity(item.getQuantity())
                            .priceAtAdd(priceAtAdd)
                            .memo(item.getMemo())
                            .build()
            );
        }
    }


    // =========================
    // ETF 상세 종목
    // =========================
    @Override
    @Transactional(readOnly = true)
    public List<MyEtfItemViewDTO> getEtfItemList(String userId, String etfName) {

        double usdRate = marketIndicatorProvider.getUsdRate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<MyEtfItemDTO> items =
                etfItemMapper.selectEtfItemList(userId, etfName, "N");

        List<MyEtfItemViewDTO> result = new ArrayList<>();

        for (MyEtfItemDTO item : items) {

            MyEtfItemViewDTO dto = MyEtfItemViewDTO.builder()
                    .id(item.getId())
                    .code(item.getCode())
                    .name(item.getName())
                    .quantity(item.getQuantity())
                    .priceAtAdd(item.getPriceAtAdd())
                    .memo(item.getMemo())
                    .build();

            if (item.getCreatedAt() != null) {
                dto.setAddedDate(item.getCreatedAt().format(dateFormatter)
                );
            }

            // 기본값
            dto.setPriceAtAddDisplay("-");
            dto.setCurrentPriceDisplay("-");
            dto.setEvaluatedAmountDisplay("-");
            dto.setProfitRateDisplay("-");

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            if (stock == null || stock.getPriceList().isEmpty() || item.getPriceAtAdd() == null) {
                result.add(dto);
                continue;
            }

            boolean isUsStock =
                    item.getCode() != null &&
                            item.getCode().matches(".*[A-Za-z].*");

            double priceAtAdd = item.getPriceAtAdd();
            double currentPrice = stock.getPriceList().get(0).getClose();

            double add = isUsStock ? priceAtAdd * usdRate : priceAtAdd;
            double current = isUsStock ? currentPrice * usdRate : currentPrice;

            double invested = add * item.getQuantity();
            double evaluated = current * item.getQuantity();

            double profitRate =
                    invested > 0
                            ? ((evaluated - invested) * 100.0) / invested
                            : 0.0;

            // 계산용
            dto.setCurrentPrice(current);
            dto.setEvaluatedAmount(evaluated);
            dto.setProfitRate(profitRate);
            dto.setPriceAtAdd(add);

            // 화면용
            dto.setPriceAtAddDisplay(String.format("%,d", Math.round(add)));
            dto.setCurrentPriceDisplay(String.format("%,d", Math.round(current)));
            dto.setEvaluatedAmountDisplay(String.format("%,d", Math.round(evaluated)));
            dto.setProfitRateDisplay(String.format("%.2f%%", profitRate));

            result.add(dto);
        }

        return result;
    }



    // =========================
    // ETF 설명
    // =========================
    @Override
    public String getEtfDescription(String userId, String etfName) {
        return etfItemMapper.selectEtfDescription(userId, etfName);
    }

    // =========================
    // ETF 편집
    // =========================
    @Override
    public void editEtf(String userId, MyEtfEditRequestDTO request) {

        if (request.getEtfDescription() != null) {
            etfItemMapper.updateEtfDescription(
                    userId,
                    request.getEtfName(),
                    request.getEtfDescription()
            );
        }

        for (MyEtfEditItemDTO dto : request.getItems()) {

            // 신규 추가
            if (dto.getId() == null && !dto.isDeleted()) {

                StockDTO stock = stockViewService.getStockInfo(null, dto.getCode());
                Double priceAtAdd = null;

                if (stock != null && !stock.getPriceList().isEmpty()) {
                    priceAtAdd = stock.getPriceList().get(0).getClose();
                }

                etfItemMapper.insertEtfItem(
                        MyEtfItemDTO.builder()
                                .userId(userId)
                                .code(dto.getCode())
                                .name(dto.getName())
                                .etfName(request.getEtfName())
                                .quantity(dto.getQuantity())
                                .priceAtAdd(priceAtAdd)
                                .build()
                );
            }

            // 삭제
            if (dto.isDeleted() && dto.getId() != null) {

                historyMapper.backupHistoryFromItem(dto.getId(), userId);
                etfItemMapper.softDeleteById(dto.getId(), userId);
            }
        }
    }

    // =========================
    // ETF 종목 복구
    // =========================
    @Override
    @Transactional
    public void restoreEtfItems(String userId, MyEtfRestoreRequestDTO request) {

        for (Long histId : request.getHistoryIds()) {

            // ✅ history → item_id 조회
            Long itemId =
                    historyMapper.selectItemIdByHistoryId(histId, userId);

            if (itemId == null) {
                throw new IllegalStateException("복구 대상 item 없음. histId=" + histId);
            }

            // ✅ item 복구 (Y → N)
            etfItemMapper.restoreById(itemId, userId);

            // ✅ history 복구 처리
            historyMapper.markRestoredById(histId, userId);
        }
    }


    // =========================
    // ETF 상세 요약
    // =========================
    @Override
    @Transactional(readOnly = true)
    public MyEtfDetailSummaryDTO getEtfDetailSummary(String userId, String etfName) {

        double usdRate = marketIndicatorProvider.getUsdRate();
        long totalInvested = 0;
        long totalEvaluated = 0;

        List<MyEtfItemDTO> items =
                etfItemMapper.selectEtfItemList(userId, etfName, "N");

        for (MyEtfItemDTO item : items) {

            if (item.getPriceAtAdd() == null) continue;

            boolean isUsStock =
                    item.getCode() != null &&
                            item.getCode().matches(".*[A-Za-z].*");

            double invested = item.getPriceAtAdd();
            if (isUsStock) invested *= usdRate;

            totalInvested += Math.round(invested * item.getQuantity());

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            if (stock == null || stock.getPriceList().isEmpty()) continue;

            double current = stock.getPriceList().get(0).getClose();
            if (isUsStock) current *= usdRate;

            totalEvaluated += Math.round(current * item.getQuantity());
        }

        long profitAmount = totalEvaluated - totalInvested;
        double profitRate =
                totalInvested > 0
                        ? (profitAmount * 100.0) / totalInvested
                        : 0.0;

        return MyEtfDetailSummaryDTO.builder()
                .totalInvested(totalInvested)
                .totalEvaluated(totalEvaluated)
                .profitAmount(profitAmount)
                .profitRate(profitRate)
                .build();
    }

    // =========================
    // ETF 전체 삭제
    // =========================
    @Override
    @Transactional
    public void deleteEtf(String userId, String etfName) {

        // 1️⃣ history 먼저 삭제
        historyMapper.deleteByUserIdAndEtfName(userId, etfName);

        // 2️⃣ item 삭제
        etfItemMapper.deleteByUserIdAndEtfName(userId, etfName);
    }


}
