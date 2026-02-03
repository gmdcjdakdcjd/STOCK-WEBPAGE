package com.stock.webpage.service.impl;

import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.MyStockMapper;
import com.stock.webpage.mapper.MyStockHistoryMapper;
import com.stock.webpage.service.MyStockService;
import com.stock.webpage.service.StockViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyStockServiceImpl implements MyStockService {

    private final MyStockMapper myStockMapper;
    private final MyStockHistoryMapper myStockHistoryMapper;
    private final StockViewService stockViewService;

    // =========================
    // 추가
    // =========================
    @Override
    public void addBatch(String userId, List<MyStockDTO> list) {

        for (MyStockDTO dto : list) {

            dto.setUserId(userId);

            Double price = dto.getPriceAtAdd();
            if (price != null) {
                dto.setTargetPrice5(Math.round(price * 1.05 * 100) / 100.0);
                dto.setTargetPrice10(Math.round(price * 1.10 * 100) / 100.0);
            }

            myStockMapper.insertMyStock(dto);

            myStockHistoryMapper.insertHistory(
                    MyStockHistoryDTO.builder()
                            .myStockId(dto.getId())
                            .userId(userId)
                            .code(dto.getCode())
                            .name(dto.getName())
                            .action("ADD")
                            .strategyName(dto.getStrategyName())
                            .specialValue(dto.getSpecialValue())
                            .priceAtAdd(dto.getPriceAtAdd())
                            .targetPrice5(dto.getTargetPrice5())
                            .targetPrice10(dto.getTargetPrice10())
                            .memo(dto.getMemo())
                            .note("전략 상세 페이지에서 추가됨")
                            .build()
            );
        }
    }

    // =========================
    // 정상 목록
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<MyStockDTO> getMyStockList(String userId) {

        List<MyStockDTO> list = myStockMapper.selectMyStockList(userId);

        for (MyStockDTO s : list) {
            StockDTO stock = stockViewService.getStockInfo(null, s.getCode());
            if (stock != null
                    && stock.getPriceList() != null
                    && !stock.getPriceList().isEmpty()) {
                s.setCurrentPrice(stock.getPriceList().get(0).getClose());
            }
        }

        return list;
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<MyStockDTO> getMyStockListKR(
            String userId,
            PageRequestDTO pageRequestDTO) {

        List<MyStockDTO> list =
                myStockMapper.selectMyStockListPagingKR(userId, pageRequestDTO);

        int total =
                myStockMapper.countMyStockKR(userId);

        for (MyStockDTO s : list) {
            StockDTO stock = stockViewService.getStockInfo(null, s.getCode());
            if (stock != null && stock.getPriceList() != null && !stock.getPriceList().isEmpty()) {
                s.setCurrentPrice(Math.floor(stock.getPriceList().get(0).getClose()));
            }
        }

        return PageResponseDTO.<MyStockDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<MyStockDTO> getMyStockListUS(
            String userId,
            PageRequestDTO pageRequestDTO) {

        List<MyStockDTO> list =
                myStockMapper.selectMyStockListPagingUS(userId, pageRequestDTO);

        int total =
                myStockMapper.countMyStockUS(userId);

        for (MyStockDTO s : list) {
            StockDTO stock = stockViewService.getStockInfo(null, s.getCode());
            if (stock != null && stock.getPriceList() != null && !stock.getPriceList().isEmpty()) {
                s.setCurrentPrice(
                        Math.floor(stock.getPriceList().get(0).getClose() * 100) / 100.0
                );
            }
        }

        return PageResponseDTO.<MyStockDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }


    private Double floor(Double v) {
        return v == null ? null : Math.floor(v);
    }

    private Double floor2(Double v) {
        return v == null ? null : Math.floor(v * 100) / 100.0;
    }


    // =========================
    // 삭제
    // =========================
    @Override
    public void delete(Long id, String userId) {

        MyStockDTO stock = myStockMapper.selectById(id);

        if (stock == null) {
            throw new IllegalArgumentException("ID not found");
        }
        if (!stock.getUserId().equals(userId)) {
            throw new SecurityException("본인 소유만 삭제 가능");
        }

        myStockMapper.softDeleteMyStock(id, userId);

        myStockHistoryMapper.insertHistory(
                MyStockHistoryDTO.builder()
                        .myStockId(id)
                        .userId(userId)
                        .code(stock.getCode())
                        .name(stock.getName())
                        .action("DELETE")
                        .note("내 종목 리스트에서 삭제됨")
                        .build()
        );
    }

    // =========================
    // 삭제 목록
    // =========================
    @Override
    @Transactional(readOnly = true)
    public List<MyStockDTO> getDeletedList(String userId) {
        return myStockMapper.selectDeletedList(userId);
    }

    // =========================
    // 복구
    // =========================
    @Override
    public void restore(Long id, String userId) {

        MyStockDTO stock = myStockMapper.selectById(id);

        if (stock == null) {
            throw new IllegalArgumentException("ID not found");
        }
        if (!stock.getUserId().equals(userId)) {
            throw new SecurityException("본인 소유만 복구 가능");
        }

        myStockMapper.restoreMyStock(id, userId);

        myStockHistoryMapper.insertHistory(
                MyStockHistoryDTO.builder()
                        .myStockId(id)
                        .userId(userId)
                        .code(stock.getCode())
                        .name(stock.getName())
                        .action("RESTORE")
                        .note("관심종목 복구")
                        .build()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<MyStockDTO> getDeletedListPaging(
            String userId,
            PageRequestDTO pageRequestDTO
    ) {

        // 1. 페이징 목록 조회
        List<MyStockDTO> list =
                myStockMapper.selectDeletedListPaging(userId, pageRequestDTO);

        // 2. 전체 건수
        int total =
                myStockMapper.countDeletedList(userId);

        // 3. PageResponseDTO 조립
        return PageResponseDTO.<MyStockDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

}
