package com.stock.webpage.service;

import com.stock.webpage.dto.StockDTO;

public interface StockViewService {

    /**
     * 종목명 또는 종목코드로 종목 정보 + 시세 조회
     */
    StockDTO getStockInfo(String stockName, String stockCode);
}
