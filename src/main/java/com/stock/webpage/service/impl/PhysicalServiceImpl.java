package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketIndicatorDTO;
import com.stock.webpage.mapper.PhysicalMapper;
import com.stock.webpage.mapper.StockIndexMapper;
import com.stock.webpage.service.PhysicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhysicalServiceImpl implements PhysicalService {

    private final PhysicalMapper physicalMapper;

    @Override
    public List<MarketIndicatorDTO> getIndicator(String code) {
        return physicalMapper.selectByCodeOrderByDateAsc(code);
    }
}
