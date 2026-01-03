package com.stock.webpage.service;

import com.stock.webpage.dto.BondDailyYieldDTO;

import java.util.List;

public interface BondService {

    List<BondDailyYieldDTO> getBondYield(String code);

}
