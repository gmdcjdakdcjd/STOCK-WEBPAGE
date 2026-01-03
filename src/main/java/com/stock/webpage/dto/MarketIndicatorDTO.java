package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketIndicatorDTO {

    private LocalDate date;
    private String code;

    private Double close;
    private Double changeAmount;
    private Double changeRate;

    private String lastUpdate;
}