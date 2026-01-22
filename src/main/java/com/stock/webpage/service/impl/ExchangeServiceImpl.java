package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketIndicatorDTO;
import com.stock.webpage.mapper.ExchangeMapper;
import com.stock.webpage.mapper.MarketIndicatorMapper;
import com.stock.webpage.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeMapper exchangeMapper;

    @Override
    public List<MarketIndicatorDTO> getIndicator(String code) {
        return exchangeMapper.selectByCodeOrderByDateAsc(code);
    }
}
