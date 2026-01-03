package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KodexEtfSummaryDTO {

    private String etfId;
    private String baseDate;

    private String etfName;
    private String irpYn;
    private Integer totalCnt;

    // DB에는 last_update만 있음 (created_at / updated_at 제거)
    private LocalDateTime lastUpdate;
}
