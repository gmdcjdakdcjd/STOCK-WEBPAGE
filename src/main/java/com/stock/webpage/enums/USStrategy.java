package com.stock.webpage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum USStrategy {

    DAILY_TOP20_VOLUME_US("상위 20 거래량 ($)"),
    DAILY_DROP_SPIKE_US("급락 스파이크 ($)"),
    DAILY_RISE_SPIKE_US("급등 스파이크 ($)"),
    DAILY_120D_NEW_HIGH_US("[일봉] 120일 신고가 ($)"),
    DAILY_120D_NEW_LOW_US("[일봉] 120일 신저가 ($)"),
    WEEKLY_52W_NEW_HIGH_US("[주봉] 52주 신고가 ($)"),
    WEEKLY_52W_NEW_LOW_US("[주봉] 52주 신저가 ($)"),
    DAILY_BB_LOWER_TOUCH_US("[일봉] 볼린저밴드 하단선 터치값 ($)"),
    DAILY_BB_UPPER_TOUCH_US("[일봉] 볼린저밴드 상단선 터치값 ($)"),
    WEEKLY_BB_LOWER_TOUCH_US("[주봉] 볼린저밴드 하단선 터치값 ($)"),
    WEEKLY_BB_UPPER_TOUCH_US("[주봉] 볼린저밴드 상단선 터치값 ($)"),
    RSI_30_UNHEATED_US("[일봉] RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_US("[일봉] RSI 상단 (70 이상) 진입값"),
    RSI_30_UNHEATED_WEEKLY_US("[주봉] RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_WEEKLY_US("[주봉] RSI 상단 (70 이상) 진입값"),
    DUAL_MOMENTUM_1M_US("듀얼모멘텀 30일 기준 ($)"),
    DUAL_MOMENTUM_3M_US("듀얼모멘텀 60일 기준 ($)"),
    DUAL_MOMENTUM_6M_US("듀얼모멘텀 180일 기준 ($)"),
    DUAL_MOMENTUM_1Y_US("듀얼모멘텀 365일 기준 ($)"),
    DAILY_TOUCH_MA20_US("[일봉] 20일선 터치값 ($)"),
    DAILY_TOUCH_MA60_US("[일봉] 60일선 터치값 ($)"),
    DAILY_TOUCH_MA120_US("[일봉] 120일선 터치값 ($)"),
    WEEKLY_TOUCH_MA20_US("[주봉] 20주선 터치값 ($)"),
    WEEKLY_TOUCH_MA60_US("[주봉] 60주선 터치값 ($)"),
    WEEKLY_TOUCH_MA120_US("[주봉] 120주선 터치값 ($)");

    private final String captureName;

    public static USStrategy from(String strategyName) {
        for (USStrategy s : values()) {
            if (s.name().equals(strategyName))
                return s;
        }
        return null;
    }
}
