package com.stock.webpage.service.impl;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.StrategyResultDTO;
import com.stock.webpage.enums.StrategyCode;
import com.stock.webpage.mapper.StrategyResultMapper;
import com.stock.webpage.service.StrategyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyResultServiceImpl implements StrategyResultService {

    private final StrategyResultMapper strategyResultMapper;

    // ============================
    // KR 전략 리스트
    // ============================
    @Override
    public PageResponseDTO<StrategyResultDTO> listKR(
            PageRequestDTO dto,
            String strategy,
            LocalDate regDate
    ) {
        return listByMarket(dto, strategy, regDate, "KR");
    }

    // ============================
    // US 전략 리스트
    // ============================
    @Override
    public PageResponseDTO<StrategyResultDTO> listUS(
            PageRequestDTO dto,
            String strategy,
            LocalDate regDate
    ) {
        return listByMarket(dto, strategy, regDate, "US");
    }

    // ============================
    // 공통 조회 로직
    // ============================
    private PageResponseDTO<StrategyResultDTO> listByMarket(
            PageRequestDTO dto,
            String strategy,
            LocalDate regDate,
            String market
    ) {

        // 마켓별 전략 코드 목록
        List<String> strategies =
                Arrays.stream(StrategyCode.values())
                        .filter(s -> s.getMarket().equals(market))
                        .map(StrategyCode::getCode)
                        .toList();

        // 전략명 정규화 (빈 문자열 → null)
        String normalizedStrategy =
                (strategy != null && !strategy.isBlank()) ? strategy : null;

        // 페이징 계산
        int offset = (dto.getPage() - 1) * dto.getSize();
        int limit = dto.getSize();

        // 리스트 조회
        List<StrategyResultDTO> list =
                strategyResultMapper.selectStrategyResults(
                        strategies,
                        normalizedStrategy,
                        regDate,
                        "_" + market,
                        offset,
                        limit
                );

        // 전체 건수 조회
        int total =
                strategyResultMapper.countStrategyResults(
                        strategies,
                        normalizedStrategy,
                        regDate,
                        "_" + market
                );

        // PageResponseDTO 생성
        return PageResponseDTO.<StrategyResultDTO>withAll()
                .pageRequestDTO(dto)
                .dtoList(list)
                .total(total)
                .build();
    }
}
