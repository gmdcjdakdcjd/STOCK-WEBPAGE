package com.stock.webpage.api;

import com.stock.webpage.dto.MarketCapDTO;
import com.stock.webpage.service.MarketCapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/marketCap")
@RequiredArgsConstructor
@Log4j2
public class ApiMarketCapController {

    private final MarketCapService marketCapService;

    @GetMapping
    public List<MarketCapDTO> getMarketCap() {
        return marketCapService.getMarketCapList();
    }


}
