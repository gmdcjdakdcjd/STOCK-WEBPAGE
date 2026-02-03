package com.stock.webpage.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KodexEtfHoldingsDTO {

    private String etfId;
    private String baseDate;
    private String stockCode;

    private String stockName;
    private BigDecimal holdingQty;
    private Integer currentPrice;
    private Long evalAmount;
    private BigDecimal weightRatio;

    private LocalDateTime lastUpdate;
}
