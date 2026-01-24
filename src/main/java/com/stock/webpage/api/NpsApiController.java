package com.stock.webpage.api;

import com.stock.webpage.service.NpsPortfolioItemService;
import com.stock.webpage.service.NpsPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nps")
public class NpsApiController {

    private final NpsPortfolioService portfolioService;
    private final NpsPortfolioItemService itemService;

    // 요약 (React)
    @GetMapping("/summary")
    public Object summary() {
        return portfolioService.getLatestSummary("NPS");
    }

    // 보유 종목 리스트 (React)
    @GetMapping("/list")
    public Object list(
            @RequestParam String asset,
            @RequestParam String market,
            @RequestParam(required = false) String q
    ) {
        return itemService.getItemList("NPS", asset, market, q);
    }

    // 자동완성 (React)
    @GetMapping("/autocomplete")
    public List<String> autocomplete(
            @RequestParam String asset,
            @RequestParam String market,
            @RequestParam String q
    ) {
        return itemService.autocompleteNames(
                "NPS", asset, market, q
        );
    }
}
