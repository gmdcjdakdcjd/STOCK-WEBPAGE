package com.stock.webpage.service.impl;

import com.stock.webpage.dto.TigerEtfSummaryDTO;
import com.stock.webpage.mapper.TigerEtfSummaryMapper;
import com.stock.webpage.service.TigerEtfHoldingsService;
import com.stock.webpage.service.TigerEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TigerEtfSummaryServiceImpl implements TigerEtfSummaryService {

    private final TigerEtfSummaryMapper summaryMapper;
    private final TigerEtfHoldingsService holdingsService;

    @Override
    public List<TigerEtfSummaryDTO> getAllSummaryList() {
        // 전체 ETF 목록
        return summaryMapper.selectAll();
    }

    @Override
    public List<TigerEtfSummaryDTO> search(String keyword) {

        // 1️. ETF명 / ETF코드
        List<TigerEtfSummaryDTO> byEtf = summaryMapper.selectByEtfNameOrEtfId(keyword);

        // 2️. 종목 → ETF ID
        List<String> etfIds = holdingsService.getEtfIdsByKeyword(keyword);

        List<TigerEtfSummaryDTO> byStock =
                etfIds.isEmpty()
                        ? List.of()
                        : summaryMapper.selectByEtfIdIn(etfIds);

        // 3️. 합치기 (중복 제거)
        Map<String, TigerEtfSummaryDTO> result = new LinkedHashMap<>();

        byEtf.forEach(dto -> result.put(dto.getEtfId(), dto));
        byStock.forEach(dto -> result.put(dto.getEtfId(), dto));

        return new ArrayList<>(result.values());
    }

    @Override
    public List<TigerEtfSummaryDTO> searchPureEtf(String keyword) {
        // ETF명 / ETF코드 명칭으로만 검색 (구성 주식 매칭 제외)
        return summaryMapper.selectByEtfNameOrEtfId(keyword);
    }
}
