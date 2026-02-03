package com.stock.webpage.common.utils;


import com.stock.webpage.dto.BondDailyYieldDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BondChartConverter {

    private BondChartConverter() {
        // util class
    }

    public static List<Map<String, Object>> convert(List<BondDailyYieldDTO> list) {
        return list.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", dto.getDate().toString());
                    map.put("close", dto.getClose());
                    map.put("open", dto.getOpen());
                    map.put("high", dto.getHigh());
                    map.put("low", dto.getLow());
                    map.put("diff", dto.getDiff());
                    return map;
                })
                .toList();
    }
}
