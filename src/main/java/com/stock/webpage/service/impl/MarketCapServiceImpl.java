package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketCapDTO;
import com.stock.webpage.mapper.MarketCapMapper;
import com.stock.webpage.service.MarketCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketCapServiceImpl implements MarketCapService {

    private final MarketCapMapper marketCapMapper;

    @Override
    public List<MarketCapDTO> getMarketCapList() {
        return marketCapMapper.selectMarketCapList();
    }
}
