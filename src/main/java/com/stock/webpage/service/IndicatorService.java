package com.stock.webpage.service;

import com.stock.webpage.dto.MarketIndicatorDTO;

import java.util.List;

public interface IndicatorService {

    List<MarketIndicatorDTO> getIndicator(String code);

}
