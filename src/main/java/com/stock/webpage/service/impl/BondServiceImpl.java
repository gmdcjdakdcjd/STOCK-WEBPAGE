package com.stock.webpage.service.impl;

import com.stock.webpage.dto.BondDailyYieldDTO;
import com.stock.webpage.mapper.BondDailyYieldMapper;
import com.stock.webpage.service.BondService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BondServiceImpl implements BondService {

    private final BondDailyYieldMapper bondDailyYieldMapper;

    @Override
    public List<BondDailyYieldDTO> getBondYield(String code) {
        return bondDailyYieldMapper.selectByCodeOrderByDateAsc(code);
    }
}
