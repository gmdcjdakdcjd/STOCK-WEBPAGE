package com.stock.webpage.api;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.enums.IndicatorType;
import com.stock.webpage.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/indicator")
@RequiredArgsConstructor
@Log4j2
public class IndicatorApiController {

    private final IndicatorService indicatorService;

    @GetMapping
    public Map<String, Object> indicator() {

        Map<String, Object> result = new HashMap<>();

        result.put("kospi",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.KOSPI.name())
                ));

        result.put("spx",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.SNP500.name())
                ));

        result.put("usd",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.USD.name())
                ));

        result.put("jpy",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.USD_JPY.name())
                ));

        result.put("goldKr",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.GOLD_KR.name())
                ));

        result.put("goldGlobal",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.GOLD_GLOBAL.name())
                ));

        result.put("wti",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.WTI.name())
                ));

        result.put("dubai",
                IndicatorChartConverter.convert(
                        indicatorService.getIndicator(IndicatorType.DUBAI.name())
                ));

        log.info("Indicator API called");

        return result;
    }
}
