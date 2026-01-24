package com.stock.webpage.api;

import com.stock.webpage.dto.StockDTO;
import com.stock.webpage.service.StockViewService;
import com.stock.webpage.service.StrategyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@Log4j2
@RequiredArgsConstructor
public class StockApiController {

    private final StockViewService stockService;
    private final StrategyDetailService strategyDetailService;

    /* ==============================
       종목 검색 (React API)
       ============================== */
    @GetMapping("/searchStock")
    public Map<String, Object> searchStock(
            @RequestParam(required = false) String stockName,
            @RequestParam(required = false) String stockCode
    ) {

        log.info("종목 검색 요청: stockName={}, stockCode={}", stockName, stockCode);

        Map<String, Object> result = new HashMap<>();

        /* ==========================
           입력값 검증
           ========================== */
        if ((stockName == null || stockName.isBlank())
                && (stockCode == null || stockCode.isBlank())) {

            result.put("error", "종목명 또는 종목코드를 입력해주세요.");
            result.put("stock", null);
            result.put("priceList", Collections.emptyList());
            result.put("signalList", Collections.emptyList());
            return result;
        }

        String keyword =
                (stockName != null && !stockName.isBlank())
                        ? stockName
                        : stockCode;

        /* ==========================
           전략 포착 이력
           ========================== */
        result.put(
                "signalList",
                strategyDetailService.searchDetail(keyword)
        );

        /* ==========================
           종목 정보
           ========================== */
        StockDTO stockInfo =
                stockService.getStockInfo(stockName, stockCode);

        log.info("stockInfo 결과: {}", stockInfo);

        if (stockInfo == null) {
            result.put("error", "해당 종목 정보를 찾을 수 없습니다.");
            result.put("stock", null);
            result.put("priceList", Collections.emptyList());
            return result;
        }

        result.put("stock", stockInfo);

        /* ==========================
           가격 데이터 가공
           (표 + 차트 공용)
           ========================== */
        boolean isKR =
                "KOSPI".equals(stockInfo.getMarketType())
                        || "KOSDAQ".equals(stockInfo.getMarketType());

        List<Map<String, Object>> priceList =
                stockInfo.getPriceList().stream()
                        .map(p -> {
                            Map<String, Object> map = new HashMap<>();

                            map.put("date", p.getDate().toString());

                            if (isKR) {
                                // 한국: 정수 처리
                                map.put("open",  (long) p.getOpen());
                                map.put("high",  (long) p.getHigh());
                                map.put("low",   (long) p.getLow());
                                map.put("close", (long) p.getClose());
                            } else {
                                // 미국: 소수점 2자리
                                map.put("open",  Math.round(p.getOpen()  * 100) / 100.0);
                                map.put("high",  Math.round(p.getHigh()  * 100) / 100.0);
                                map.put("low",   Math.round(p.getLow()   * 100) / 100.0);
                                map.put("close", Math.round(p.getClose() * 100) / 100.0);
                            }

                            map.put("volume", p.getVolume());
                            return map;
                        })
                        .toList();

        result.put("priceList", priceList);

        return result;
    }
}
