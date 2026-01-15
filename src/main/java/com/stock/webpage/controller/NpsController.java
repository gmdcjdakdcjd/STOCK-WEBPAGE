package com.stock.webpage.controller;

import com.stock.webpage.service.NpsPortfolioService;
import com.stock.webpage.service.NpsPortfolioItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NpsController {

    private final NpsPortfolioService portfolioService;
    private final NpsPortfolioItemService itemService;

    // 국민연금 요약
    @GetMapping("/nps/summary")
    public String summary(Model model) {

        model.addAttribute(
                "list",
                portfolioService.getLatestSummary("NPS")
        );

        return "nps/summary";
    }

    // 국민연금 보유 종목 리스트
    @GetMapping("/nps/list")
    public String list(
            @RequestParam String asset,
            @RequestParam String market,
            @RequestParam(required = false) String q,
            Model model
    ) {

        model.addAttribute(
                "list",
                itemService.getItemList("NPS", asset, market, q)
        );

        model.addAttribute("asset", asset);
        model.addAttribute("market", market);
        model.addAttribute("q", q); // 화면 검색어 유지

        return "nps/list";
    }

    @GetMapping("/nps/autocomplete")
    @ResponseBody
    public List<String> autocomplete(
            @RequestParam String asset,
            @RequestParam String market,
            @RequestParam String q
    ) {
        return itemService.autocompleteNames(
                "NPS",
                asset,
                market,
                q
        );
    }

}
