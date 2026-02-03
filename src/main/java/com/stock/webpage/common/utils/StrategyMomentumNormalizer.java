package com.stock.webpage.common.utils;
import com.stock.webpage.dto.StrategyDetailDTO;

import java.util.List;

public class StrategyMomentumNormalizer {

    private StrategyMomentumNormalizer() {
        // util class
    }

    public static List<StrategyDetailDTO> normalize(List<StrategyDetailDTO> list, boolean isKR) {
        return list.stream().map(item -> {

            double price = item.getPrice();
            double prev = item.getPrevClose();
            double diff = item.getDiff();

            if (isKR) {
                // 한국: 정수 처리 (236500.0 방지)
                item.setPrice((double) ((long) price));
                item.setPrevClose((double) ((long) prev));
                item.setDiff((double) ((long) diff));
            } else {
                // 미국: 소수점 2자리 버림
                item.setPrice(Math.floor(price * 100) / 100.0);
                item.setPrevClose(Math.floor(prev * 100) / 100.0);
                item.setDiff(Math.floor(diff * 100) / 100.0);
            }

            return item;
        }).toList();
    }
}
