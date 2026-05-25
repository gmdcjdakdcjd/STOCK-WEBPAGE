package com.stock.webpage.api;

import com.stock.webpage.dto.TigerEtfHoldingsDTO;
import com.stock.webpage.dto.TigerEtfSummaryDTO;
import com.stock.webpage.service.TigerEtfHoldingsService;
import com.stock.webpage.service.TigerEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tiger")
public class TigerEtfApiController {

    private final TigerEtfSummaryService tigerEtfSummaryService;
    private final TigerEtfHoldingsService tigerEtfHoldingsService;

    // Tiger ETF 요약 (React)
    @GetMapping("/summary")
    public List<TigerEtfSummaryDTO> summary(
            @RequestParam(required = false) String q
    ) {
        return (q == null || q.isBlank())
                ? tigerEtfSummaryService.getAllSummaryList()
                : tigerEtfSummaryService.search(q);
    }

    // ETF 구성 종목 (React)
    @GetMapping("/holdings")
    public List<TigerEtfHoldingsDTO> holdings(
            @RequestParam String etfId
    ) {
        return tigerEtfHoldingsService.getHoldingsByEtfId(etfId);
    }
}
