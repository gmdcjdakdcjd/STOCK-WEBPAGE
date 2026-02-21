package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositTrendDTO {

    private Long id;
    private LocalDateTime baseDate;

    private Long custDeposit;
    private Long custDepositChange;
    private boolean custDepositUp;

    private Long creditBalance;
    private Long creditBalanceChange;
    private boolean creditBalanceUp;

    private Long stockFund;
    private Long stockFundChange;
    private boolean stockFundUp;

    private Long mixedFund;
    private Long mixedFundChange;
    private boolean mixedFundUp;

    private Long bondFund;
    private Long bondFundChange;
    private boolean bondFundUp;

    private LocalDateTime lastUpdate;
}