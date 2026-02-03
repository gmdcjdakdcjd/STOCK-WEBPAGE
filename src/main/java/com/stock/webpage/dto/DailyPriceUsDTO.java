package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPriceUsDTO {

    private String code;
    private LocalDate date;

    private Double open;
    private Double high;
    private Double low;
    private Double close;

    private Long volume;
    private LocalDateTime lastUpdate;
}
