package com.stock.webpage.service.impl;

import com.stock.webpage.dto.KodexEtfHoldingsDTO;
import com.stock.webpage.mapper.KodexEtfHoldingsMapper;
import com.stock.webpage.service.KodexEtfHoldingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KodexEtfHoldingsServiceImpl implements KodexEtfHoldingsService {

    private final KodexEtfHoldingsMapper holdingsMapper;

    @Override
    public List<KodexEtfHoldingsDTO> getHoldingsByEtfId(String etfId) {
        return holdingsMapper.selectHoldingsByEtfIdOrderByWeight(etfId);
    }

    @Override
    public List<String> getEtfIdsByKeyword(String keyword) {
        return holdingsMapper.selectEtfIdsByKeyword(keyword);
    }
}
