package com.stock.webpage.service.impl;

import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.*;
import com.stock.webpage.service.StockViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockViewServiceImpl implements StockViewService {

    private final CompanyInfoKrMapper companyInfoKrMapper;
    private final CompanyInfoUsMapper companyInfoUsMapper;
    private final DailyPriceKrMapper dailyPriceKrMapper;
    private final DailyPriceUsMapper dailyPriceUsMapper;

    @Override
    public StockDTO getStockInfo(String stockName, String stockCode) {

        CompanyInfoKrDTO kr = null;
        CompanyInfoUsDTO us = null;

        // =========================
        // 종목 기본 정보 조회
        // =========================
        if (stockCode != null && !stockCode.isBlank()) {

            CompanyInfoKrDTO krTmp =
                    companyInfoKrMapper.selectByCode(stockCode);

            if (krTmp != null) {
                kr = krTmp;
            } else {
                us = companyInfoUsMapper.selectByCode(stockCode);
            }

        } else if (stockName != null && !stockName.isBlank()) {

            List<CompanyInfoKrDTO> krList =
                    companyInfoKrMapper.selectTopByNameOrCodeContaining(stockName, 1);

            if (!krList.isEmpty()) {
                kr = krList.get(0);
            } else {
                List<CompanyInfoUsDTO> usList =
                        companyInfoUsMapper.selectTopByNameOrCodeContaining(stockName, 1);

                if (!usList.isEmpty()) {
                    us = usList.get(0);
                }
            }
        }


        if (kr == null && us == null) {
            return null;
        }

        // =========================
        // 한국 주식
        // =========================
        if (kr != null) {
            List<PriceDTO> prices =
                    dailyPriceKrMapper.selectByCodeOrderByDateDesc(kr.getCode())
                            .stream()
                            .map(p -> PriceDTO.builder()
                                    .date(p.getDate())
                                    .open(p.getOpen())
                                    .high(p.getHigh())
                                    .low(p.getLow())
                                    .close(p.getClose())
                                    .volume(p.getVolume())
                                    .build())
                            .toList();

            // 차트 표시를 위한 1년치 데이터 (최대 365개)
            List<PriceDTO> chartPrices = prices.stream()
                    .limit(365)
                    .toList();

            // 가격 테이블 표시를 위한 최초 데이터 (최대 20개)
            List<PriceDTO> tablePrices = prices.stream()
                    .limit(20)
                    .toList();

            return StockDTO.builder()
                    .code(kr.getCode())
                    .name(kr.getName())
                    .marketType(kr.getMarketType()) // KOSPI / KOSDAQ
                    .companyInfoKr(kr)
                    .chartPriceList(chartPrices)
                    .priceList(tablePrices)
                    .build();
        }

        // =========================
        // 미국 주식
        // =========================
        if (us == null) {
            return null;
        }

        List<PriceDTO> prices =
                dailyPriceUsMapper.selectByCodeOrderByDateDesc(us.getCode())
                        .stream()
                        .map(p -> PriceDTO.builder()
                                .date(p.getDate())
                                .open(p.getOpen())
                                .high(p.getHigh())
                                .low(p.getLow())
                                .close(p.getClose())
                                .volume(p.getVolume())
                                .build())
                        .toList();

        // 차트 표시를 위한 1년치 데이터 (최대 365개)
        List<PriceDTO> chartPrices = prices.stream()
                .limit(365)
                .toList();

        // 가격 테이블 표시를 위한 최초 데이터 (최대 20개)
        List<PriceDTO> tablePrices = prices.stream()
                .limit(20)
                .toList();

        return StockDTO.builder()
                .code(us.getCode())
                .name(us.getName())
                .marketType(us.getMarket()) // NASDAQ / NYSE
                .companyInfoUs(us)
                .chartPriceList(chartPrices)
                .priceList(tablePrices)
                .build();
    }

    /**
     * 특정 종목코드의 가격 데이터를 페이징 처리하여 조회합니다.
     *
     * @param stockCode 종목코드
     * @param page 페이지 번호 (1부터 시작)
     * @param size 한 페이지당 조회할 개수
     * @return 가공되지 않은 가격 정보 리스트
     */
    @Override
    public List<PriceDTO> getPricePage(String stockCode, int page, int size) {
        int limit = size;
        int offset = (page - 1) * size;

        // 종목 코드가 한국 주식 마켓 정보에 등록되어 있는지 확인합니다.
        boolean isKr = companyInfoKrMapper.selectByCode(stockCode) != null;

        if (isKr) {
            // 한국 주식일 경우 일별 주가 테이블에서 페이징 쿼리를 수행합니다.
            return dailyPriceKrMapper.selectPricePageByCode(stockCode, limit, offset)
                    .stream()
                    .map(p -> PriceDTO.builder()
                            .date(p.getDate())
                            .open(p.getOpen())
                            .high(p.getHigh())
                            .low(p.getLow())
                            .close(p.getClose())
                            .volume(p.getVolume())
                            .build())
                    .toList();
        } else {
            // 미국 주식일 경우 일별 주가 테이블에서 페이징 쿼리를 수행합니다.
            return dailyPriceUsMapper.selectPricePageByCode(stockCode, limit, offset)
                    .stream()
                    .map(p -> PriceDTO.builder()
                            .date(p.getDate())
                            .open(p.getOpen())
                            .high(p.getHigh())
                            .low(p.getLow())
                            .close(p.getClose())
                            .volume(p.getVolume())
                            .build())
                    .toList();
        }
    }
}
