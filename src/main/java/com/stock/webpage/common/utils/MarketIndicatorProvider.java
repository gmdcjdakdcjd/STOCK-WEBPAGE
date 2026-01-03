package com.stock.webpage.common.utils;

import com.stock.webpage.mapper.MarketIndicatorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketIndicatorProvider {

    private final MarketIndicatorMapper marketIndicatorMapper;

    public double getUsdRate() {

        Double rate = marketIndicatorMapper.selectLatestCloseByCode("usd");

        if (rate == null) {
            throw new IllegalStateException("USD 환율 정보 없음");
        }

        return rate;
    }
}
