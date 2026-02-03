package com.stock.webpage.api;

import com.stock.webpage.dto.KodexEtfHoldingsDTO;
import com.stock.webpage.dto.KodexEtfSummaryDTO;
import com.stock.webpage.service.KodexEtfHoldingsService;
import com.stock.webpage.service.KodexEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kodex")
public class KodexEtfApiController {

    private final KodexEtfSummaryService kodexEtfSummaryService;
    private final KodexEtfHoldingsService kodexEtfHoldingsService;

    // KODEX ETF 요약 (React)
    @GetMapping("/summary")
    public List<KodexEtfSummaryDTO> summary(
            @RequestParam(required = false) String q
    ) {
        return (q == null || q.isBlank())
                ? kodexEtfSummaryService.getAllSummaryList()
                : kodexEtfSummaryService.search(q);
    }

    // ETF 구성 종목 (React)
    @GetMapping("/holdings")
    public List<KodexEtfHoldingsDTO> holdings(
            @RequestParam String etfId
    ) {
        return kodexEtfHoldingsService.getHoldingsByEtfId(etfId);
    }
}
