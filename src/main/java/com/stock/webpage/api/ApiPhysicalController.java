package com.stock.webpage.api;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.enums.PhysicalType;
import com.stock.webpage.service.PhysicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/physical")
@RequiredArgsConstructor
@Log4j2
public class ApiPhysicalController {

    private final PhysicalService physicalService;

    @GetMapping
    public Map<String, Object> indicator() {

        Map<String, Object> result = new HashMap<>();

        result.put("BEAN",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.BEAN.name())
                ));

        result.put("COFFEE",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.COFFEE.name())
                ));

        result.put("COPPER",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.COPPER.name())
                ));

        result.put("CORN",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.CORN.name())
                ));

        result.put("COTTON",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.COTTON.name())
                ));

        result.put("DUBAI",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.DUBAI.name())
                ));

        result.put("GOLD_GLOBAL",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.GOLD_GLOBAL.name())
                ));

        result.put("GOLD_KR",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.GOLD_KR.name())
                ));

        result.put("RICE",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.RICE.name())
                ));

        result.put("SILVER",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.SILVER.name())
                ));

        result.put("SUGAR",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.SUGAR.name())
                ));

        result.put("WTI",
                IndicatorChartConverter.convert(
                        physicalService.getIndicator(PhysicalType.WTI.name())
                ));

        log.info("Physical API called");

        return result;
    }
}
