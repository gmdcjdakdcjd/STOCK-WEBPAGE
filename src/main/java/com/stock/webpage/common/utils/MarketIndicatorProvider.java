package com.stock.webpage.common.utils;

import com.stock.webpage.mapper.ExchangeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketIndicatorProvider {

    private final ExchangeMapper exchangeMapper;

    public double getUsdRate() {

        Double rate = exchangeMapper.selectLatestCloseByCode("USDKRW");

        if (rate == null) {
            throw new IllegalStateException("USD 환율 정보 없음");
        }

        return rate;
    }
}
