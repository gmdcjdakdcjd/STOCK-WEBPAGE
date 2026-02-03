package com.stock.webpage.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyEtfItemViewDTO {

    private Long id;

    private String code;
    private String name;

    private Integer quantity;

    private Double priceAtAdd;
    private Double currentPrice;
    private Double evaluatedAmount;

    private Double profitRate;

    // 화면 표시용
    private String priceAtAddDisplay;
    private String currentPriceDisplay;
    private String evaluatedAmountDisplay;
    private String profitRateDisplay;

    private String addedDate;   // yyyy-MM-dd

    private String memo;

    private String market; // "KR" | "US"
}
