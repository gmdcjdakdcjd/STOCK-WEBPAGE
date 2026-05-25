package com.stock.webpage.service;

import com.stock.webpage.dto.TigerEtfHoldingsDTO;
import java.util.List;

public interface TigerEtfHoldingsService {

    // ETF → 종목 (모달)
    List<TigerEtfHoldingsDTO> getHoldingsByEtfId(String etfId);

    // 종목 → ETF ID 목록 (중간 단계)
    List<String> getEtfIdsByKeyword(String keyword);
}
