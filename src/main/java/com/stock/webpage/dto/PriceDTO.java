package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PriceDTO {

    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}
