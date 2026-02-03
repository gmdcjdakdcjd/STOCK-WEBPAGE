package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketIndicatorDTO;
import com.stock.webpage.mapper.MarketIndicatorMapper;
import com.stock.webpage.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorServiceImpl implements IndicatorService {

    private final MarketIndicatorMapper marketIndicatorMapper;

    @Override
    public List<MarketIndicatorDTO> getIndicator(String code) {
        return marketIndicatorMapper.selectByCodeOrderByDateAsc(code);
    }
}