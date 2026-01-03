package com.stock.webpage.controller;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.StrategyDetailDTO;
import com.stock.webpage.dto.StrategyResultDTO;
import com.stock.webpage.enums.KRStrategy;
import com.stock.webpage.enums.StrategyCode;
import com.stock.webpage.enums.USStrategy;
import com.stock.webpage.service.StrategyDetailService;
import com.stock.webpage.service.StrategyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyResultService strategyResultService;
    private final StrategyDetailService strategyDetailService;

    // ==============================
    // KR 전략 목록
    // ==============================
    @GetMapping("/listKR")
    public String listKR(
            PageRequestDTO pageRequestDTO,
            @RequestParam(value = "strategy", required = false) String strategy,
            @RequestParam(value = "regDate", required = false) LocalDate regDate,
            Model model) {

        model.addAttribute("strategyList",
                Arrays.stream(StrategyCode.values())
                        .filter(s -> s.getMarket().equals("KR"))
                        .toList()
        );

        PageResponseDTO<StrategyResultDTO> responseDTO =
                strategyResultService.listKR(pageRequestDTO, strategy, regDate);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("strategy", strategy);
        model.addAttribute("regDate", regDate);

        return "board/resultListKR";
    }

    // ==============================
    // KR 전략 상세
    // ==============================
    @GetMapping("/detailKR")
    public String detailKR(
            @RequestParam("strategy") String strategy,
            @RequestParam("date") LocalDate date,
            Model model) {

        List<StrategyDetailDTO> list =
                strategyDetailService.getDetail(strategy, date);

        model.addAttribute("priceLabel", resolvePriceLabel(strategy));
        model.addAttribute("captureName", resolveKRCaptureName(strategy));
        model.addAttribute("strategy", strategy);
        model.addAttribute("date", date);
        model.addAttribute("detailList", list);

        return "board/detailKR";
    }

    // ==============================
    // US 전략 목록
    // ==============================
    @GetMapping("/listUS")
    public String listUS(
            PageRequestDTO pageRequestDTO,
            @RequestParam(value = "strategy", required = false) String strategy,
            @RequestParam(value = "regDate", required = false) LocalDate regDate,
            Model model) {

        model.addAttribute("strategyList",
                Arrays.stream(StrategyCode.values())
                        .filter(s -> s.getMarket().equals("US"))
                        .toList()
        );

        PageResponseDTO<StrategyResultDTO> responseDTO =
                strategyResultService.listUS(pageRequestDTO, strategy, regDate);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("strategy", strategy);
        model.addAttribute("regDate", regDate);

        return "board/resultListUS";
    }

    // ==============================
    // US 전략 상세
    // ==============================
    @GetMapping("/detailUS")
    public String detailUS(
            @RequestParam("strategy") String strategy,
            @RequestParam("date") LocalDate date,
            Model model) {

        List<StrategyDetailDTO> list =
                strategyDetailService.getDetail(strategy, date);

        // 미국: 소수점 2자리 절삭
        List<StrategyDetailDTO> normalized = list.stream()
                .map(dto -> {
                    dto.setPrice(truncate2(dto.getPrice()));
                    dto.setPrevClose(truncate2(dto.getPrevClose()));
                    dto.setDiff(truncate2(dto.getDiff()));
                    return dto;
                })
                .toList();

        model.addAttribute("priceLabel", resolvePriceLabel(strategy));
        model.addAttribute("captureName", resolveUSCaptureName(strategy));
        model.addAttribute("strategy", strategy);
        model.addAttribute("date", date);
        model.addAttribute("detailList", normalized);

        return "board/detailUS";
    }

    // ==============================
    // 공통 유틸 메서드
    // ==============================
    private String resolvePriceLabel(String strategy) {
        return strategy.contains("WEEKLY") ? "전주종가" : "전일종가";
    }

    private String resolveKRCaptureName(String strategy) {
        KRStrategy krStrategy = KRStrategy.from(strategy);
        return (krStrategy != null) ? krStrategy.getCaptureName() : "포착값";
    }

    private String resolveUSCaptureName(String strategy) {
        USStrategy usStrategy = USStrategy.from(strategy);
        return (usStrategy != null) ? usStrategy.getCaptureName() : "포착값";
    }

    private double truncate2(double value) {
        return Math.floor(value * 100) / 100.0;
    }
}
