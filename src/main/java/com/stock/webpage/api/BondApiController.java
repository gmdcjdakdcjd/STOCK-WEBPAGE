package com.stock.webpage.api;

import com.stock.webpage.common.utils.BondChartConverter;
import com.stock.webpage.service.BondService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bond")
@RequiredArgsConstructor
@Log4j2
public class BondApiController {

    private final BondService bondService;

    @GetMapping
    public Map<String, Object> getBondData() {

        Map<String, Object> result = new HashMap<>();

        // 🇺🇸 미국
        result.put("us2y",  BondChartConverter.convert(bondService.getBondYield("US2YT=X")));
        result.put("us5y",  BondChartConverter.convert(bondService.getBondYield("US5YT=X")));
        result.put("us10y", BondChartConverter.convert(bondService.getBondYield("US10YT=X")));
        result.put("us30y", BondChartConverter.convert(bondService.getBondYield("US30YT=X")));

        // 🇰🇷 한국
        result.put("kr3y",  BondChartConverter.convert(bondService.getBondYield("KR3YT=RR")));
        result.put("kr5y",  BondChartConverter.convert(bondService.getBondYield("KR5YT=RR")));
        result.put("kr10y", BondChartConverter.convert(bondService.getBondYield("KR10YT=RR")));
        result.put("kr20y", BondChartConverter.convert(bondService.getBondYield("KR20YT=RR")));

        // 🇯🇵 일본
        result.put("jp2y",  BondChartConverter.convert(bondService.getBondYield("JP2YT=XX")));
        result.put("jp5y",  BondChartConverter.convert(bondService.getBondYield("JP5YT=XX")));
        result.put("jp10y", BondChartConverter.convert(bondService.getBondYield("JP10YT=XX")));
        result.put("jp30y", BondChartConverter.convert(bondService.getBondYield("JP30YT=XX")));

        // 🇨🇳 중국
        result.put("cn1y",  BondChartConverter.convert(bondService.getBondYield("CN1YT=RR")));
        result.put("cn3y",  BondChartConverter.convert(bondService.getBondYield("CN3YT=RR")));
        result.put("cn5y",  BondChartConverter.convert(bondService.getBondYield("CN5YT=RR")));
        result.put("cn10y", BondChartConverter.convert(bondService.getBondYield("CN10YT=RR")));

        log.info("Bond API called");

        return result;
    }
}
