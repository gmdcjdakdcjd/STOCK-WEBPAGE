package com.stock.webpage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyCode {

    // ============================
    // KR 전략
    // ============================
    DAILY_TOP20_VOLUME_KR("DAILY_TOP20_VOLUME_KR", "상위 20 거래량 KR", "KR"),
    DAILY_DROP_SPIKE_KR("DAILY_DROP_SPIKE_KR", "급락 스파이크 KR", "KR"),
    DAILY_RISE_SPIKE_KR("DAILY_RISE_SPIKE_KR", "급등 스파이크 KR", "KR"),
    DAILY_120D_NEW_HIGH_KR("DAILY_120D_NEW_HIGH_KR", "[일봉] 120일 신고가 KR", "KR"),
    DAILY_120D_NEW_LOW_KR("DAILY_120D_NEW_LOW_KR", "[일봉] 120일 신저가 KR", "KR"),
    WEEKLY_52W_NEW_HIGH_KR("WEEKLY_52W_NEW_HIGH_KR", "[주봉] 52주 신고가 KR", "KR"),
    WEEKLY_52W_NEW_LOW_KR("WEEKLY_52W_NEW_LOW_KR", "[주봉] 52주 신저가 KR", "KR"),
    DAILY_BB_LOWER_TOUCH_KR("DAILY_BB_LOWER_TOUCH_KR", "[일봉] 볼린저밴드 하단선 터치값 KR", "KR"),
    DAILY_BB_UPPER_TOUCH_KR("DAILY_BB_UPPER_TOUCH_KR", "[일봉] 볼린저밴드 상단선 터치값 KR", "KR"),
    WEEKLY_BB_LOWER_TOUCH_KR("WEEKLY_BB_LOWER_TOUCH_KR", "[주봉] 볼린저밴드 하단선 터치값 KR", "KR"),
    WEEKLY_BB_UPPER_TOUCH_KR("WEEKLY_BB_UPPER_TOUCH_KR", "[주봉] 볼린저밴드 상단선 터치값 KR", "KR"),
    RSI_30_UNHEATED_KR("RSI_30_UNHEATED_KR", "[일봉] RSI 하단 (30 이하) 진입값", "KR"),
    RSI_70_OVERHEATED_KR("RSI_70_OVERHEATED_KR", "[일봉] RSI 상단 (70 이상) 진입값", "KR"),
    RSI_30_UNHEATED_WEEKLY_KR("RSI_30_UNHEATED_WEEKLY_KR", "[주봉] RSI 하단 (30 이하) 진입값", "KR"),
    RSI_70_OVERHEATED_WEEKLY_KR("RSI_70_OVERHEATED_WEEKLY_KR", "[주봉] RSI 상단 (70 이상) 진입값", "KR"),
    DUAL_MOMENTUM_1M_KR("DUAL_MOMENTUM_1M_KR", "듀얼모멘텀 30일 기준 KR", "KR"),
    DUAL_MOMENTUM_3M_KR("DUAL_MOMENTUM_3M_KR", "듀얼모멘텀 60일 기준 KR", "KR"),
    DUAL_MOMENTUM_6M_KR("DUAL_MOMENTUM_6M_KR", "듀얼모멘텀 180일 기준 KR", "KR"),
    DUAL_MOMENTUM_1Y_KR("DUAL_MOMENTUM_1Y_KR", "듀얼모멘텀 365일 기준 KR", "KR"),
    DAILY_TOUCH_MA20_KR("DAILY_TOUCH_MA20_KR", "[일봉] 20일선 터치값 KR", "KR"),
    DAILY_TOUCH_MA60_KR("DAILY_TOUCH_MA60_KR", "[일봉] 60일선 터치값 KR", "KR"),
    DAILY_TOUCH_MA120_KR("DAILY_TOUCH_MA120_KR", "[일봉] 120일선 터치값 KR", "KR"),
    WEEKLY_TOUCH_MA20_KR("WEEKLY_TOUCH_MA20_KR", "[주봉] 20주선 터치값 KR", "KR"),
    WEEKLY_TOUCH_MA60_KR("WEEKLY_TOUCH_MA60_KR", "[주봉] 60주선 터치값 KR", "KR"),
    WEEKLY_TOUCH_MA120_KR("WEEKLY_TOUCH_MA120_KR", "[주봉] 120주선 터치값 KR", "KR"),

    // ============================
    // US 전략
    // ============================
    DAILY_TOP20_VOLUME_US("DAILY_TOP20_VOLUME_US", "상위 20 거래량 US", "US"),
    DAILY_DROP_SPIKE_US("DAILY_DROP_SPIKE_US", "급락 스파이크 US", "US"),
    DAILY_RISE_SPIKE_US("DAILY_RISE_SPIKE_US", "급등 스파이크 US", "US"),
    DAILY_120D_NEW_HIGH_US("DAILY_120D_NEW_HIGH_US", "[일봉] 120일 신고가 US", "US"),
    DAILY_120D_NEW_LOW_US("DAILY_120D_NEW_LOW_US", "[일봉] 120일 신저가 US", "US"),
    WEEKLY_52W_NEW_HIGH_US("WEEKLY_52W_NEW_HIGH_US", "[주봉] 52주 신고가 US", "US"),
    WEEKLY_52W_NEW_LOW_US("WEEKLY_52W_NEW_LOW_US", "[주봉] 52주 신저가 US", "US"),
    DAILY_BB_LOWER_TOUCH_US("DAILY_BB_LOWER_TOUCH_US", "[일봉] 볼린저밴드 하단선 터치값 US", "US"),
    DAILY_BB_UPPER_TOUCH_US("DAILY_BB_UPPER_TOUCH_US", "[일봉] 볼린저밴드 상단선 터치값 US", "US"),
    WEEKLY_BB_LOWER_TOUCH_US("WEEKLY_BB_LOWER_TOUCH_US", "[주봉] 볼린저밴드 하단선 터치값 US", "US"),
    WEEKLY_BB_UPPER_TOUCH_US("WEEKLY_BB_UPPER_TOUCH_US", "[주봉] 볼린저밴드 상단선 터치값 US", "US"),
    RSI_30_UNHEATED_US("RSI_30_UNHEATED_US", "[일봉] RSI 하단 (30 이하) 진입값", "US"),
    RSI_70_OVERHEATED_US("RSI_70_OVERHEATED_US", "[일봉] RSI 상단 (70 이상) 진입값", "US"),
    RSI_30_UNHEATED_WEEKLY_US("RSI_30_UNHEATED_WEEKLY_US", "[주봉] RSI 하단 (30 이하) 진입값", "US"),
    RSI_70_OVERHEATED_WEEKLY_US("RSI_70_OVERHEATED_WEEKLY_US", "[주봉] RSI 상단 (70 이상) 진입값", "US"),
    DUAL_MOMENTUM_1M_US("DUAL_MOMENTUM_1M_US", "듀얼모멘텀 30일 기준 US", "US"),
    DUAL_MOMENTUM_3M_US("DUAL_MOMENTUM_3M_US", "듀얼모멘텀 60일 기준 US", "US"),
    DUAL_MOMENTUM_6M_US("DUAL_MOMENTUM_6M_US", "듀얼모멘텀 180일 기준 US", "US"),
    DUAL_MOMENTUM_1Y_US("DUAL_MOMENTUM_1Y_US", "듀얼모멘텀 365일 기준 US", "US"),
    DAILY_TOUCH_MA20_US("DAILY_TOUCH_MA20_US", "[일봉] 20일선 터치값 US", "US"),
    DAILY_TOUCH_MA60_US("DAILY_TOUCH_MA60_US", "[일봉] 60일선 터치값 US", "US"),
    DAILY_TOUCH_MA120_US("DAILY_TOUCH_MA120_US", "[일봉] 120일선 터치값 US", "US"),
    WEEKLY_TOUCH_MA20_US("WEEKLY_TOUCH_MA20_US", "[주봉] 20주선 터치값 US", "US"),
    WEEKLY_TOUCH_MA60_US("WEEKLY_TOUCH_MA60_US", "[주봉] 60주선 터치값 US", "US"),
    WEEKLY_TOUCH_MA120_US("WEEKLY_TOUCH_MA120_US", "[주봉] 120주선 터치값 US", "US");

    private final String code;   // DB 저장용
    private final String label;  // 화면 표시용 한국어
    private final String market; // KR or US

    // =============== 헬퍼 메서드 ===============
    public static StrategyCode findByCode(String code) {
        for (StrategyCode sc : values()) {
            if (sc.code.equals(code)) return sc;
        }
        return null;
    }
}
