package com.stock.webpage.service.impl;

import com.stock.webpage.dto.KodexEtfSummaryDTO;
import com.stock.webpage.mapper.KodexEtfSummaryMapper;
import com.stock.webpage.service.KodexEtfHoldingsService;
import com.stock.webpage.service.KodexEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KodexEtfSummaryServiceImpl implements KodexEtfSummaryService {

    private final KodexEtfSummaryMapper summaryMapper;
    private final KodexEtfHoldingsService holdingsService;

    @Override
    public List<KodexEtfSummaryDTO> getAllSummaryList() {
        // 전체 ETF 목록
        return summaryMapper.selectAll();
    }

    @Override
    public List<KodexEtfSummaryDTO> search(String keyword) {

        // 1️. ETF명 / ETF코드
        List<KodexEtfSummaryDTO> byEtf = summaryMapper.selectByEtfNameOrEtfId(keyword);

        // 2️. 종목 → ETF ID
        List<String> etfIds = holdingsService.getEtfIdsByKeyword(keyword);

        List<KodexEtfSummaryDTO> byStock =
                etfIds.isEmpty()
                        ? List.of()
                        : summaryMapper.selectByEtfIdIn(etfIds);

        // 3️. 합치기 (중복 제거)
        Map<String, KodexEtfSummaryDTO> result = new LinkedHashMap<>();

        byEtf.forEach(dto -> result.put(dto.getEtfId(), dto));
        byStock.forEach(dto -> result.put(dto.getEtfId(), dto));

        return new ArrayList<>(result.values());
    }

}
