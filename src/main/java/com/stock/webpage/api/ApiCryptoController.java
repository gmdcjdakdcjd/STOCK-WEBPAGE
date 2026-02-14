package com.stock.webpage.api;

import com.stock.webpage.common.utils.IndicatorChartConverter;
import com.stock.webpage.enums.CryptoType;
import com.stock.webpage.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
@Log4j2
public class ApiCryptoController {

    private final CryptoService cryptoService;

    @GetMapping
    public Map<String, Object> indicator() {

        Map<String, Object> result = new HashMap<>();

        result.put("bitcoin",
                IndicatorChartConverter.convert(
                        cryptoService.getIndicator(CryptoType.BTC.name())
                ));

        result.put("ethereum",
                IndicatorChartConverter.convert(
                        cryptoService.getIndicator(CryptoType.ETH.name())
                ));

        result.put("solana",
                IndicatorChartConverter.convert(
                        cryptoService.getIndicator(CryptoType.SOL.name())
                ));

        result.put("stablecoin",
                IndicatorChartConverter.convert(
                        cryptoService.getIndicator(CryptoType.XRP.name())
                ));

        result.put("binance",
                IndicatorChartConverter.convert(
                        cryptoService.getIndicator(CryptoType.BNB.name())
                ));

        log.info("Crypto API called");

        return result;
    }
}
