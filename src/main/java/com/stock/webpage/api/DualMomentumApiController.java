package com.stock.webpage.api;

import com.stock.webpage.common.utils.StrategyMomentumNormalizer;
import com.stock.webpage.dto.StrategyDetailDTO;
import com.stock.webpage.service.StrategyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dual-momentum")
@RequiredArgsConstructor
@Log4j2
public class DualMomentumApiController {

    private final StrategyDetailService strategyDetailService;

    @GetMapping
    public Map<String, List<StrategyDetailDTO>> dualMomentum() {

        String[] strategies = {
                "DUAL_MOMENTUM_1M_KR",
                "DUAL_MOMENTUM_3M_KR",
                "DUAL_MOMENTUM_6M_KR",
                "DUAL_MOMENTUM_1Y_KR",
                "DUAL_MOMENTUM_1M_US",
                "DUAL_MOMENTUM_3M_US",
                "DUAL_MOMENTUM_6M_US",
                "DUAL_MOMENTUM_1Y_US"
        };

        Map<String, List<StrategyDetailDTO>> data = new LinkedHashMap<>();
        String today = LocalDate.now().toString();

        for (String s : strategies) {
            List<StrategyDetailDTO> raw =
                    strategyDetailService.getLatestOrToday(s, today);

            boolean isKR = s.endsWith("_KR");
            List<StrategyDetailDTO> normalized =
                    StrategyMomentumNormalizer.normalize(raw, isKR);

            data.put(s, normalized);
        }

        return data;
    }
}
