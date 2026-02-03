package com.stock.webpage.controller;

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
public class IssueController {

    private final StrategyDetailService strategyDetailService;

    @GetMapping("/issue")
    public String issue(Model model) {

        String[] strategies = {
                "DAILY_DROP_SPIKE_KR",
                "DAILY_RISE_SPIKE_KR",
                "DAILY_TOP20_VOLUME_KR",
                "ETF_TOP20_VOLUME_KR",
                "DAILY_DROP_SPIKE_US",
                "DAILY_RISE_SPIKE_US",
                "DAILY_TOP20_VOLUME_US",
                "ETF_TOP20_VOLUME_US"
        };

        Map<String, List<StrategyDetailDTO>> data = new LinkedHashMap<>();

        String today = LocalDate.now().toString();

        for (String s : strategies) {
            List<StrategyDetailDTO> list = strategyDetailService.getLatestOrToday(s, today);
            data.put(s, list);
        }

        model.addAttribute("strategyMap", data);

        return "board/issue";
    }
}
