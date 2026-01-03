package com.stock.webpage.controller;

import com.stock.webpage.common.utils.BondChartConverter;
import com.stock.webpage.service.BondService;
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
public class BondController {

    private final BondService bondService;

    @GetMapping("/bond")
    public String bond(Model model) {

        // ------------------------------
        // 미국 금리 (2Y, 5Y, 10Y, 30Y)
        // ------------------------------
        var us2y = bondService.getBondYield("US2YT=X");
        var us5y = bondService.getBondYield("US5YT=X");
        var us10y = bondService.getBondYield("US10YT=X");
        var us30y = bondService.getBondYield("US30YT=X");

        // ------------------------------
        // 한국 금리 (3Y, 5Y, 10Y, 20Y)
        // ------------------------------
        var kr3y = bondService.getBondYield("KR3YT=RR");
        var kr5y = bondService.getBondYield("KR5YT=RR");
        var kr10y = bondService.getBondYield("KR10YT=RR");
        var kr20y = bondService.getBondYield("KR20YT=RR");

        // ------------------------------
        // 일본 금리 (2Y, 5Y, 10Y, 30Y)
        // ------------------------------
        var jp2y = bondService.getBondYield("JP2YT=XX");
        var jp5y = bondService.getBondYield("JP5YT=XX");
        var jp10y = bondService.getBondYield("JP10YT=XX");
        var jp30y = bondService.getBondYield("JP30YT=XX");

        // ------------------------------
        // 중국 금리 (1Y, 3Y, 5Y, 10Y)
        // ------------------------------
        var cn1y = bondService.getBondYield("CN1YT=RR");
        var cn3y = bondService.getBondYield("CN3YT=RR");
        var cn5y = bondService.getBondYield("CN5YT=RR");
        var cn10y = bondService.getBondYield("CN10YT=RR");

        // ------------------------------
        // Thymeleaf 전달
        // ------------------------------
        model.addAttribute("us2y", BondChartConverter.convert(us2y));
        model.addAttribute("us5y", BondChartConverter.convert(us5y));
        model.addAttribute("us10y", BondChartConverter.convert(us10y));
        model.addAttribute("us30y", BondChartConverter.convert(us30y));

        model.addAttribute("kr3y", BondChartConverter.convert(kr3y));
        model.addAttribute("kr5y", BondChartConverter.convert(kr5y));
        model.addAttribute("kr10y", BondChartConverter.convert(kr10y));
        model.addAttribute("kr20y", BondChartConverter.convert(kr20y));

        model.addAttribute("jp2y", BondChartConverter.convert(jp2y));
        model.addAttribute("jp5y", BondChartConverter.convert(jp5y));
        model.addAttribute("jp10y", BondChartConverter.convert(jp10y));
        model.addAttribute("jp30y", BondChartConverter.convert(jp30y));

        model.addAttribute("cn1y", BondChartConverter.convert(cn1y));
        model.addAttribute("cn3y", BondChartConverter.convert(cn3y));
        model.addAttribute("cn5y", BondChartConverter.convert(cn5y));
        model.addAttribute("cn10y", BondChartConverter.convert(cn10y));

        log.info("US 10Y → {}", us10y);
        log.info("KR 10Y → {}", kr10y);

        return "board/bond";
    }
}
