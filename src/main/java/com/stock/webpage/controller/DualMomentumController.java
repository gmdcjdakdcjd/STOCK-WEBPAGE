package com.stock.webpage.controller;

import com.stock.webpage.common.utils.StrategyMomentumNormalizer;
import com.stock.webpage.dto.StrategyDetailDTO;
import com.stock.webpage.service.StrategyDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class DualMomentumController {

    private final StrategyDetailService strategyDetailService;

    @GetMapping("/dualMomentumList")
    public String dualMomentumList(Model model) {

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

        model.addAttribute("strategyMap", data);
        return "board/dualMomentum";
    }
}
