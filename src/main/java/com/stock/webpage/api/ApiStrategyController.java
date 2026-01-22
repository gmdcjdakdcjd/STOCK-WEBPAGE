package com.stock.webpage.api;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.StrategyCodeDTO;
import com.stock.webpage.dto.StrategyDetailDTO;
import com.stock.webpage.dto.StrategyResultDTO;
import com.stock.webpage.enums.KRStrategy;
import com.stock.webpage.enums.StrategyCode;
import com.stock.webpage.enums.USStrategy;
import com.stock.webpage.service.StrategyDetailService;
import com.stock.webpage.service.StrategyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ApiStrategyController {

    private final StrategyResultService strategyResultService;
    private final StrategyDetailService strategyDetailService;

    /* ==============================
       KR 전략 목록
       ============================== */
    @GetMapping("/kr")
    public Map<String, Object> listKR(
            PageRequestDTO pageRequestDTO,
            @RequestParam(required = false) String strategy,
            @RequestParam(required = false) LocalDate regDate
    ) {

        PageResponseDTO<StrategyResultDTO> response =
                strategyResultService.listKR(pageRequestDTO, strategy, regDate);

        List<StrategyCodeDTO> strategyList =
                Arrays.stream(StrategyCode.values())
                        .filter(s -> "KR".equals(s.getMarket()))
                        .map(s -> new StrategyCodeDTO(
                                s.getCode(),
                                s.getLabel()
                        ))
                        .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("market", "KR");
        result.put("strategyList", strategyList);
        result.put("response", response);
        result.put("strategy", strategy);
        result.put("regDate", regDate);

        return result;
    }

    /* ==============================
       KR 전략 상세
       ============================== */
    @GetMapping("/kr/detail")
    public Map<String, Object> detailKR(
            @RequestParam String strategy,
            @RequestParam LocalDate date
    ) {

        List<StrategyDetailDTO> list =
                strategyDetailService.getDetail(strategy, date);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("market", "KR");
        result.put("strategy", strategy);
        result.put("date", date);
        result.put("priceLabel", resolvePriceLabel(strategy));
        result.put("captureName", resolveKRCaptureName(strategy));
        result.put("detailList", list);

        return result;
    }

    /* ==============================
       US 전략 목록
       ============================== */
    @GetMapping("/us")
    public Map<String, Object> listUS(
            PageRequestDTO pageRequestDTO,
            @RequestParam(required = false) String strategy,
            @RequestParam(required = false) LocalDate regDate
    ) {

        PageResponseDTO<StrategyResultDTO> response =
                strategyResultService.listUS(pageRequestDTO, strategy, regDate);

        List<StrategyCodeDTO> strategyList =
                Arrays.stream(StrategyCode.values())
                        .filter(s -> "US".equals(s.getMarket()))
                        .map(s -> new StrategyCodeDTO(
                                s.getCode(),
                                s.getLabel()
                        ))
                        .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("market", "US");
        result.put("strategyList", strategyList);
        result.put("response", response);
        result.put("strategy", strategy);
        result.put("regDate", regDate);

        return result;
    }

    /* ==============================
       US 전략 상세
       ============================== */
    @GetMapping("/us/detail")
    public Map<String, Object> detailUS(
            @RequestParam String strategy,
            @RequestParam LocalDate date
    ) {

        List<StrategyDetailDTO> list =
                strategyDetailService.getDetail(strategy, date);

        List<StrategyDetailDTO> normalized = list.stream()
                .map(dto -> {
                    dto.setPrice(truncate2(dto.getPrice()));
                    dto.setPrevClose(truncate2(dto.getPrevClose()));
                    dto.setDiff(truncate2(dto.getDiff()));
                    return dto;
                })
                .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("market", "US");
        result.put("strategy", strategy);
        result.put("date", date);
        result.put("priceLabel", resolvePriceLabel(strategy));
        result.put("captureName", resolveUSCaptureName(strategy));
        result.put("detailList", normalized);

        return result;
    }

    /* ==============================
       공통 유틸
       ============================== */
    private String resolvePriceLabel(String strategy) {
        return strategy.contains("WEEKLY") ? "전주종가" : "전일종가";
    }

    private String resolveKRCaptureName(String strategy) {
        KRStrategy krStrategy = KRStrategy.from(strategy);
        return krStrategy != null ? krStrategy.getCaptureName() : "포착값";
    }

    private String resolveUSCaptureName(String strategy) {
        USStrategy usStrategy = USStrategy.from(strategy);
        return usStrategy != null ? usStrategy.getCaptureName() : "포착값";
    }

    private double truncate2(double value) {
        return Math.floor(value * 100) / 100.0;
    }
}
