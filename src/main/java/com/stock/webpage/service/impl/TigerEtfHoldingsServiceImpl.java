package com.stock.webpage.service.impl;

import com.stock.webpage.dto.TigerEtfHoldingsDTO;
import com.stock.webpage.mapper.TigerEtfHoldingsMapper;
import com.stock.webpage.service.TigerEtfHoldingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TigerEtfHoldingsServiceImpl implements TigerEtfHoldingsService {

    private final TigerEtfHoldingsMapper holdingsMapper;

    @Override
    public List<TigerEtfHoldingsDTO> getHoldingsByEtfId(String etfId) {
        return holdingsMapper.selectHoldingsByEtfIdOrderByWeight(etfId);
    }

    @Override
    public List<String> getEtfIdsByKeyword(String keyword) {
        return holdingsMapper.selectEtfIdsByKeyword(keyword);
    }
}
