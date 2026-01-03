package com.stock.webpage.controller;

import com.stock.webpage.dto.KodexEtfHoldingsDTO;
import com.stock.webpage.dto.KodexEtfSummaryDTO;
import com.stock.webpage.service.KodexEtfHoldingsService;
import com.stock.webpage.service.KodexEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kodex")
public class KodexEtfSummaryController {

    private final KodexEtfSummaryService kodexEtfSummaryService;
    private final KodexEtfHoldingsService kodexEtfHoldingsService;

    /**
     * KODEX ETF 요약 목록 화면
     */
    @GetMapping("/summary")
    public String summaryList(
            @RequestParam(required = false) String q,
            Model model
    ) {
        List<KodexEtfSummaryDTO> list =
                (q == null || q.isBlank())
                        ? kodexEtfSummaryService.getAllSummaryList()
                        : kodexEtfSummaryService.search(q);

        model.addAttribute("list", list);
        model.addAttribute("q", q);

        return "kodex/summary";
    }


    /**
     * ETF 구성 종목 (AJAX)
     */
    @GetMapping("/holdings")
    @ResponseBody
    public List<KodexEtfHoldingsDTO> holdings(@RequestParam String etfId) {
        return kodexEtfHoldingsService.getHoldingsByEtfId(etfId);
    }
}
