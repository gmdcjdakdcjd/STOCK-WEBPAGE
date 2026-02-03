package com.stock.webpage.api;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.enums.ExchangeType;
import com.stock.webpage.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
@Log4j2
public class ApiExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    public Map<String, Object> indicator() {

        Map<String, Object> result = new HashMap<>();

        result.put("cny",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.CNYKRW.name())
                ));

        result.put("eur",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.EURKRW.name())
                ));

        result.put("gbp",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.GBPKRW.name())
                ));

        result.put("hkd",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.HKDKRW.name())
                ));

        result.put("jpy",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.JPYKRW.name())
                ));

        result.put("twd",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.TWDKRW.name())
                ));

        result.put("usd",
                IndicatorChartConverter.convert(
                        exchangeService.getIndicator(ExchangeType.USDKRW.name())
                ));

        log.info("Exchange API called");

        return result;
    }
}
