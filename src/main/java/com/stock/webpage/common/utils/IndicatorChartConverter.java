package com.stock.webpage.common.utils;

import com.stock.webpage.dto.MarketIndicatorDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicatorChartConverter {

    private IndicatorChartConverter() {
        // util class
    }

    public static List<Map<String, Object>> convert(List<MarketIndicatorDTO> list) {
        return list.stream()
                .map(dto -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("date", dto.getDate().toString());
                    m.put("close", dto.getClose());
                    return m;
                })
                .toList();
    }
}
