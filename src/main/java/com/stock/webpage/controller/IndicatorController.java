package com.stock.webpage.controller;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;

    @GetMapping("/indicator")
    public String indicator(Model model) {

        var kospiList = indicatorService.getIndicator("KOSPI");
        var spxList = indicatorService.getIndicator("SNP500");
        var usdList = indicatorService.getIndicator("USD");
        var jpyList = indicatorService.getIndicator("USD_JPY");
        var goldKrList = indicatorService.getIndicator("GOLD_KR");
        var goldGlobalList = indicatorService.getIndicator("GOLD_GLOBAL");
        var wtiList = indicatorService.getIndicator("WTI");
        var dubaiList = indicatorService.getIndicator("DUBAI");

        log.info("KOSPI   → {}", kospiList);
        log.info("SPX     → {}", spxList);
        log.info("USD     → {}", usdList);
        log.info("JPY     → {}", jpyList);
        log.info("GOLD_KR → {}", goldKrList);
        log.info("GOLD_GL → {}", goldGlobalList);
        log.info("WTI     → {}", wtiList);
        log.info("DUBAI   → {}", dubaiList);

        model.addAttribute("kospiList", IndicatorChartConverter.convert(kospiList));
        model.addAttribute("spxList", IndicatorChartConverter.convert(spxList));
        model.addAttribute("usdList", IndicatorChartConverter.convert(usdList));
        model.addAttribute("jpyList", IndicatorChartConverter.convert(jpyList));
        model.addAttribute("goldKrList", IndicatorChartConverter.convert(goldKrList));
        model.addAttribute("goldGlobalList", IndicatorChartConverter.convert(goldGlobalList));
        model.addAttribute("wtiList", IndicatorChartConverter.convert(wtiList));
        model.addAttribute("dubaiList", IndicatorChartConverter.convert(dubaiList));

        IndicatorChartConverter.convert(kospiList)
                .forEach(m ->
                        log.info("Converted → date={}, close={}", m.get("date"), m.get("close"))
                );

        return "board/indicator";
    }
}
