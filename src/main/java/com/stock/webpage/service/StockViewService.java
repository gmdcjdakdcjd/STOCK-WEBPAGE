package com.stock.webpage.service;

import com.stock.webpage.dto.StockDTO;

public interface StockViewService {

    /**
     * 종목명 또는 종목코드로 종목 정보 + 시세 조회
     */
    StockDTO getStockInfo(String stockName, String stockCode);

    /**
     * 특정 종목코드의 가격 데이터를 페이징 처리하여 조회합니다.
     *
     * @param stockCode 종목코드
     * @param page 페이지 번호 (1부터 시작)
     * @param size 한 페이지당 조회할 개수
     * @return 가공되지 않은 가격 정보 리스트
     */
    java.util.List<com.stock.webpage.dto.PriceDTO> getPricePage(String stockCode, int page, int size);
}
