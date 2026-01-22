package com.stock.webpage.service;

import com.stock.webpage.dto.MarketIndicatorDTO;

import java.util.List;

public interface StockIndexService {
    List<MarketIndicatorDTO> getIndicator(String code);
}
