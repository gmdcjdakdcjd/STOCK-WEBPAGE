package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TigerEtfSummaryDTO {

    private String etfId;
    private String baseDate;
    private Integer totalCnt;
    private String etfName;
    private LocalDateTime lastUpdate;
}