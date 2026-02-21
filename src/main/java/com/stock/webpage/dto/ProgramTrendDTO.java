package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramTrendDTO {

    private Long id;

    private LocalDateTime baseDate;

    // 차익거래
    private Long arbBuy;
    private Long arbSell;
    private Long arbNet;

    // 비차익거래
    private Long nonarbBuy;
    private Long nonarbSell;
    private Long nonarbNet;

    // 전체
    private Long totalBuy;
    private Long totalSell;
    private Long totalNet;

    private LocalDateTime lastUpdate;
}