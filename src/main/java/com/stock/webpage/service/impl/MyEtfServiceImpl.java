package com.stock.webpage.service.impl;

import com.stock.webpage.common.utils.MarketIndicatorProvider;
import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.CompanyInfoKrMapper;
import com.stock.webpage.mapper.CompanyInfoUsMapper;
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
    private final CompanyInfoKrMapper companyInfoKrMapper;
    private final CompanyInfoUsMapper companyInfoUsMapper;

    // =========================
    // ETF 목록 요약
    // =========================
    @Override
    public PageResponseDTO<MyEtfSummaryDTO> getMyEtfList(
            String userId,
            PageRequestDTO pageRequestDTO
    ) {

        // 전체 ETF 개수 (페이징용)
        int total = etfItemMapper.countMyEtf(userId);

        // 페이징된 ETF 요약 목록
        List<Map<String, Object>> rows =
                etfItemMapper.selectMyEtfSummaryPaging(userId, pageRequestDTO);

        double usdRate = marketIndicatorProvider.getUsdRate();
        List<MyEtfSummaryDTO> dtoList = new ArrayList<>();

        // 기존 계산 로직 그대로 유지
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

        // PageResponseDTO로 감싸서 반환
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

        // 1. ETF 이름 중복 체크
        int count = etfItemMapper.countByUserIdAndEtfName(
                userId,
                request.getEtfName()
        );

        if (count > 0) {
            throw new IllegalStateException("이미 존재하는 ETF 이름입니다.");
        }

        // 2. 정상 생성
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
                    .priceAtAdd(item.getPriceAtAdd()) // 원본값 (KR: KRW / US: USD)
                    .memo(item.getMemo())
                    .build();

            if (item.getCreatedAt() != null) {
                dto.setAddedDate(item.getCreatedAt().format(dateFormatter));
            }

            // =========================
            // 기본값
            // =========================
            dto.setPriceAtAddDisplay("-");
            dto.setCurrentPriceDisplay("-");
            dto.setEvaluatedAmountDisplay("-");
            dto.setProfitRateDisplay("-");

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            if (stock == null || stock.getPriceList().isEmpty() || item.getPriceAtAdd() == null) {
                result.add(dto);
                continue;
            }

            // =========================
            // 시장 구분
            // =========================
            boolean isUsStock = isUsStock(item.getCode());

            dto.setMarket(isUsStock ? "US" : "KR");

            // =========================
            // 원본 가격 (절대 환전하지 않음)
            // =========================
            double priceAtAddRaw = item.getPriceAtAdd();                  // KR: KRW / US: USD
            double currentPriceRaw = stock.getPriceList().get(0).getClose();

            dto.setPriceAtAdd(priceAtAddRaw);
            dto.setCurrentPrice(currentPriceRaw);

            // =========================
            // KRW 환산 (계산/표시 전용)
            // =========================
            double addKrw     = isUsStock ? priceAtAddRaw * usdRate : priceAtAddRaw;
            double currentKrw = isUsStock ? currentPriceRaw * usdRate : currentPriceRaw;

            double invested  = addKrw * item.getQuantity();
            double evaluated = currentKrw * item.getQuantity();

            double profitRate =
                    invested > 0
                            ? ((evaluated - invested) * 100.0) / invested
                            : 0.0;

            // =========================
            // 계산 결과
            // =========================
            dto.setProfitRate(profitRate);
            // 필요하면 숫자 계산용으로 사용 가능
            // dto.setEvaluatedAmount(evaluated);

            // =========================
            // 화면 표시용 (KRW)
            // =========================
            dto.setPriceAtAddDisplay(String.format("%,d", Math.round(addKrw)));
            dto.setCurrentPriceDisplay(String.format("%,d", Math.round(currentKrw)));
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

            // history → item_id 조회
            Long itemId = historyMapper.selectItemIdByHistoryId(histId, userId);

            if (itemId == null) {
                throw new IllegalStateException("복구 대상 item 없음. histId=" + histId);
            }

            // item 복구 (Y → N)
            etfItemMapper.restoreById(itemId, userId);

            // history 복구 처리
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

            boolean isUsStock = isUsStock(item.getCode());


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

        // history 먼저 삭제
        historyMapper.deleteByUserIdAndEtfName(userId, etfName);

        // item 삭제
        etfItemMapper.deleteByUserIdAndEtfName(userId, etfName);
    }

    @Override
    public List<MyEtfItemHistoryDTO> getEtfItemRestoreHistory(
            String userId,
            String etfName
    ) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        double usdRate = marketIndicatorProvider.getUsdRate();

        List<MyEtfItemHistoryDTO> list =
                historyMapper.selectByUserIdEtfNameRestoredYn(
                        userId,
                        etfName,
                        "N"
                );

        for (MyEtfItemHistoryDTO dto : list) {

            // =========================
            // 날짜 포맷
            // =========================
            if (dto.getCreatedAt() != null) {
                dto.setCreatedAtDisplay(
                        dto.getCreatedAt().format(formatter)
                );
            }

            if (dto.getDeletedAt() != null) {
                dto.setDeletedAtDisplay(
                        dto.getDeletedAt().format(formatter)
                );
            }

            // =========================
            // 시장 구분
            // =========================
            boolean isUsStock = isUsStock(dto.getCode());


            dto.setMarket(isUsStock ? "US" : "KR");

            // =========================
            // 가격 처리
            // =========================
            if (dto.getPriceAtAdd() != null) {

                double priceAtAddRaw = dto.getPriceAtAdd(); // KR: KRW / US: USD
                double priceAtAddKrw =
                        isUsStock ? priceAtAddRaw * usdRate : priceAtAddRaw;

                // KRW 표시용
                dto.setPriceAtAddDisplay(
                        String.format("%,d", Math.round(priceAtAddKrw))
                );

                // USD 원본은 dto.getPriceAtAdd() 그대로 유지
            }
        }

        return list;
    }

    private boolean isUsStock(String code) {

        if (code == null) return false;

        if (companyInfoKrMapper.selectByCode(code) != null) {
            return false;
        }

        return companyInfoUsMapper.selectByCode(code) != null;// UNKNOWN → 기본 KR 취급
    }


}
