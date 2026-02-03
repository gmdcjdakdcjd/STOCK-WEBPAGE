package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketIndicatorDTO;
import com.stock.webpage.mapper.StockIndexMapper;
import com.stock.webpage.service.StockIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockIndexServiceImpl implements StockIndexService {

    private final StockIndexMapper stockIndexMapper;

    @Override
    public List<MarketIndicatorDTO> getIndicator(String code) {
        return stockIndexMapper.selectByCodeOrderByDateAsc(code);
    }
}
