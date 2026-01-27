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

            return StockDTO.builder()
                    .code(kr.getCode())
                    .name(kr.getName())
                    .marketType(kr.getMarketType()) // KOSPI / KOSDAQ
                    .companyInfoKr(kr)
                    .priceList(prices)
                    .build();
        }

        // =========================
        // 미국 주식
        // =========================
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

        return StockDTO.builder()
                .code(us.getCode())
                .name(us.getName())
                .marketType(us.getMarket()) // NASDAQ / NYSE
                .companyInfoUs(us)
                .priceList(prices)
                .build();
    }
}
