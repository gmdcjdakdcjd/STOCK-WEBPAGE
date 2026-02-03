package com.stock.webpage.service;

import com.stock.webpage.dto.KodexEtfHoldingsDTO;
import java.util.List;

public interface KodexEtfHoldingsService {

    // ETF → 종목 (모달)
    List<KodexEtfHoldingsDTO> getHoldingsByEtfId(String etfId);

    // 종목 → ETF ID 목록 (중간 단계)
    List<String> getEtfIdsByKeyword(String keyword);
}
