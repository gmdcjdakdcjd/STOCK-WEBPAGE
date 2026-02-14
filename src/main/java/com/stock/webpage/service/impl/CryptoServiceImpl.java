package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MarketIndicatorDTO;
import com.stock.webpage.mapper.CryptoMapper;
import com.stock.webpage.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private final CryptoMapper cryptoMapper;

    @Override
    public List<MarketIndicatorDTO> getIndicator(String code) {
        return cryptoMapper.selectByCodeOrderByDateAsc(code);
    }
}
