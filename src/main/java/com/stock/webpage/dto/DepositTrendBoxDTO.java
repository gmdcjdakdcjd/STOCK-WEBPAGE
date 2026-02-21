package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositTrendBoxDTO {

    private LocalDateTime baseDate;   // 기준 날짜

    private Long custDeposit;
    private Long custDepositDiff;
    private boolean custDepositUp;

    private Long creditBalance;
    private Long creditBalanceDiff;
    private boolean creditBalanceUp;

    // =========================
    // 개별 펀드
    // =========================

    private Long stockFund;
    private Long stockFundDiff;
    private boolean stockFundUp;

    private Long mixedFund;
    private Long mixedFundDiff;
    private boolean mixedFundUp;

    private Long bondFund;
    private Long bondFundDiff;
    private boolean bondFundUp;

    // =========================
    // 펀드 총합
    // =========================

    private Long totalFund;
    private Long totalFundDiff;
    private boolean totalFundUp;
}