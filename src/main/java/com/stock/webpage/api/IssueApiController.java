package com.stock.webpage.api;

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
@RequestMapping("/api/issue")
@RequiredArgsConstructor
@Log4j2
public class IssueApiController {

    private final StrategyDetailService strategyDetailService;

    @GetMapping
    public Map<String, List<StrategyDetailDTO>> issue() {

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
            List<StrategyDetailDTO> list =
                    strategyDetailService.getLatestOrToday(s, today);
            data.put(s, list);
        }

        return data;
    }
}
