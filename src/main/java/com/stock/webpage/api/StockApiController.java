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
           전략 포착 이력 (최초 10개 페이징 조회)
           ========================== */
        result.put(
                "signalList",
                strategyDetailService.searchDetailPaged(keyword, 1, 10)
        );

        /* ==========================
           종목 정보
           ========================== */
        StockDTO stockInfo =
                stockService.getStockInfo(stockName, stockCode);

        log.info("stockInfo 조회가 완료되었습니다.");

        if (stockInfo == null) {
            result.put("error", "해당 종목 정보를 찾을 수 없습니다.");
            result.put("stock", null);
            result.put("priceList", Collections.emptyList());
            result.put("chartPriceList", Collections.emptyList());
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

        // 가격 테이블용 데이터 가공 (최대 20개)
        result.put("priceList", convertPriceList(stockInfo.getPriceList(), isKR));

        // 차트 표시용 데이터 가공 (최대 365개)
        result.put("chartPriceList", convertPriceList(stockInfo.getChartPriceList(), isKR));

        return result;
    }

    /**
     * 주가 리스트 데이터를 화면 표시 형식(한국 주식 정수 처리 / 미국 주식 소수점 2자리 처리)에 맞추어 변환합니다.
     *
     * @param sourceList 원본 PriceDTO 리스트
     * @param isKR 한국 주식 여부
     * @return 가공된 맵 리스트
     */
    private List<Map<String, Object>> convertPriceList(List<com.stock.webpage.dto.PriceDTO> sourceList, boolean isKR) {
        if (sourceList == null) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();

                    map.put("date", p.getDate().toString());

                    if (isKR) {
                        // 한국 주식: 소수점을 버리고 정수형으로 처리
                        map.put("open",  (long) p.getOpen());
                        map.put("high",  (long) p.getHigh());
                        map.put("low",   (long) p.getLow());
                        map.put("close", (long) p.getClose());
                    } else {
                        // 미국 주식: 달러 단위 소수점 둘째 자리까지 반올림 처리
                        map.put("open",  Math.round(p.getOpen()  * 100) / 100.0);
                        map.put("high",  Math.round(p.getHigh()  * 100) / 100.0);
                        map.put("low",   Math.round(p.getLow()   * 100) / 100.0);
                        map.put("close", Math.round(p.getClose() * 100) / 100.0);
                    }

                    map.put("volume", p.getVolume());
                    return map;
                })
                .toList();
    }

    /* ==============================
       종목 가격 페이징 조회 (React API)
       ============================== */
    @GetMapping("/prices")
    public List<Map<String, Object>> getPrices(
            @RequestParam String code,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("종목 가격 페이징 요청: code={}, page={}, size={}", code, page, size);

        // 마켓 타입(한국/미국) 판별을 위해 종목 기본 정보를 가져옵니다.
        StockDTO stockInfo = stockService.getStockInfo(null, code);
        if (stockInfo == null) {
            return Collections.emptyList();
        }

        boolean isKR = "KOSPI".equals(stockInfo.getMarketType())
                || "KOSDAQ".equals(stockInfo.getMarketType());

        // 페이징 처리된 원본 데이터를 조회합니다.
        List<com.stock.webpage.dto.PriceDTO> pricePage = stockService.getPricePage(code, page, size);

        // 한국/미국 주식 포맷에 맞추어 변환하여 리턴합니다.
        return convertPriceList(pricePage, isKR);
    }

    /* ==============================
       종목 조건 포착 정보 페이징 조회 (React API)
       ============================== */
    @GetMapping("/signals")
    public List<com.stock.webpage.dto.StrategyDetailDTO> getSignals(
            @RequestParam String code,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("종목 조건 포착 정보 페이징 요청: code={}, page={}, size={}", code, page, size);

        // 페이징 처리된 조건 포착(시그널) 이력을 조회하여 반환합니다.
        return strategyDetailService.searchDetailPaged(code, page, size);
    }
}
