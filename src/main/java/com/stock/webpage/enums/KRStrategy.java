package com.stock.webpage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KRStrategy {

    DAILY_TOP20_VOLUME_KR("상위 20 거래량 KR"),
    DAILY_DROP_SPIKE_KR("급락 스파이크 KR"),
    DAILY_RISE_SPIKE_KR("급등 스파이크 KR"),
    DAILY_120D_NEW_HIGH_KR("[일봉] 120일 신고가 KR"),
    DAILY_120D_NEW_LOW_KR("[일봉] 120일 신저가 KR"),
    WEEKLY_52W_NEW_HIGH_KR("[주봉] 52주 신고가 KR"),
    WEEKLY_52W_NEW_LOW_KR("[주봉] 52주 신저가 KR"),
    DAILY_BB_LOWER_TOUCH_KR("[일봉] 볼린저밴드 하단선 터치값 KR"),
    DAILY_BB_UPPER_TOUCH_KR("[일봉] 볼린저밴드 상단선 터치값 KR"),
    WEEKLY_BB_LOWER_TOUCH_KR("[주봉] 볼린저밴드 하단선 터치값 KR"),
    WEEKLY_BB_UPPER_TOUCH_KR("[주봉] 볼린저밴드 상단선 터치값 KR"),
    RSI_30_UNHEATED_KR("[일봉] RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_KR("[일봉] RSI 상단 (70 이상) 진입값"),
    RSI_30_UNHEATED_WEEKLY_KR("[주봉] RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_WEEKLY_KR("[주봉] RSI 상단 (70 이상) 진입값"),
    DUAL_MOMENTUM_1M_KR("듀얼모멘텀 30일 기준 KR"),
    DUAL_MOMENTUM_3M_KR("듀얼모멘텀 60일 기준 KR"),
    DUAL_MOMENTUM_6M_KR("듀얼모멘텀 180일 기준 KR"),
    DUAL_MOMENTUM_1Y_KR("듀얼모멘텀 365일 기준 KR"),
    DAILY_TOUCH_MA20_KR("[일봉] 20일선 터치값 KR"),
    DAILY_TOUCH_MA60_KR("[일봉] 60일선 터치값 KR"),
    DAILY_TOUCH_MA120_KR("[일봉] 120일선 터치값 KR"),
    WEEKLY_TOUCH_MA20_KR("[주봉] 20주선 터치값 KR"),
    WEEKLY_TOUCH_MA60_KR("[주봉] 60주선 터치값 KR"),
    WEEKLY_TOUCH_MA120_KR("[주봉] 120주선 터치값 KR");



    private final String captureName;

    public static KRStrategy from(String strategyName) {
        for (KRStrategy s : values()) {
            if (s.name().equals(strategyName)) return s;
        }
        return null;
    }
}
