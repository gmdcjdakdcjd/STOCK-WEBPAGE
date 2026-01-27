package com.stock.webpage.api;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.enums.StockIndexType;
import com.stock.webpage.service.StockIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stockIndex")
@RequiredArgsConstructor
@Log4j2
public class ApiStockIndexController {

    private final StockIndexService stockIndexService;

    @GetMapping
    public Map<String, Object> indicator() {

        Map<String, Object> result = new HashMap<>();

        result.put("dow",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.DJI.name())
                ));

        result.put("hongkong",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.HSI.name())
                ));

        result.put("italy",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.ITI.name())
                ));

        result.put("kosdaq",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.KDQ.name())
                ));

        result.put("kospi",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.KSP.name())
                ));

        result.put("england",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.LNS.name())
                ));

        result.put("nasdaq",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.NAS.name())
                ));

        result.put("japan",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.NII.name())
                ));

        result.put("france",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.PAS.name())
                ));

        result.put("china",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.SHS.name())
                ));

        result.put("euro",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.STX.name())
                ));

        result.put("taiwan",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.TWS.name())
                ));

        result.put("german",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.XTR.name())
                ));

        result.put("snp500",
                IndicatorChartConverter.convert(
                        stockIndexService.getIndicator(StockIndexType.SPI.name())
                ));

        log.info("StockIndex API called");

        return result;
    }
}
