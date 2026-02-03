package com.stock.webpage.controller;

import com.stock.webpage.dto.*;
import com.stock.webpage.service.StockViewService;
import com.stock.webpage.service.StrategyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class StockController {

    private final StockViewService stockService;
    private final StrategyDetailService strategyDetailService;

    @GetMapping("/stockInfo")
    public String stockInfo() {
        return "board/stockInfo";
    }

    @GetMapping("/searchStock")
    public String searchStock(
            @RequestParam(required = false) String stockName,
            @RequestParam(required = false) String stockCode,
            Model model) {

        log.info("🔍 종목 검색 요청: stockName={}, stockCode={}", stockName, stockCode);

        // 1️⃣ 입력값 검증
        if ((stockName == null || stockName.isBlank())
                && (stockCode == null || stockCode.isBlank())) {

            model.addAttribute("error", "종목명 또는 종목코드를 입력해주세요.");
            return "board/stockInfo";
        }

        String keyword =
                (stockName != null && !stockName.isBlank()) ? stockName : stockCode;

        // 2️⃣ 전략 포착 이력
        model.addAttribute(
                "signalList",
                strategyDetailService.searchDetail(keyword)
        );

        // 3️⃣ 종목 정보
        StockDTO stockInfo = stockService.getStockInfo(stockName, stockCode);
        log.info("✅ stockInfo 결과: {}", stockInfo);

        if (stockInfo == null) {
            model.addAttribute("error", "해당 종목 정보를 찾을 수 없습니다.");
            model.addAttribute("stock", null);
            model.addAttribute("priceList", Collections.emptyList());
            return "board/stockInfo";
        }

        model.addAttribute("stock", stockInfo);

        // 4️⃣ 가격 데이터 가공 (표 + 차트 공용)
        boolean isKR =
                "KOSPI".equals(stockInfo.getMarketType())
                        || "KOSDAQ".equals(stockInfo.getMarketType());

        List<Map<String, Object>> priceList =
                stockInfo.getPriceList().stream()
                        .map(p -> {
                            Map<String, Object> map = new HashMap<>();

                            map.put("date", p.getDate().toString());

                            if (isKR) {
                                map.put("open",  (long) p.getOpen());
                                map.put("high",  (long) p.getHigh());
                                map.put("low",   (long) p.getLow());
                                map.put("close", (long) p.getClose());
                            } else {
                                map.put("open",  Math.round(p.getOpen()  * 100) / 100.0);
                                map.put("high",  Math.round(p.getHigh()  * 100) / 100.0);
                                map.put("low",   Math.round(p.getLow()   * 100) / 100.0);
                                map.put("close", Math.round(p.getClose() * 100) / 100.0);
                            }

                            map.put("volume", p.getVolume());
                            return map;
                        })
                        .toList();

        model.addAttribute("priceList", priceList);

        return "board/stockInfo";
    }
}
