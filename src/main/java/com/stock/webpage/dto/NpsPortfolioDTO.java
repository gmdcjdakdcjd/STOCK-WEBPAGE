package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NpsPortfolioDTO {

    /** 기관 코드 (NPS) */
    private String institutionCode;

    /** 기준일 */
    private LocalDate baseDate;

    /** 자산 유형 (STOCK | ETF | BOND) */
    private String assetType;

    /** 시장 (KR | US | GLOBAL) */
    private String market;

    /** 전체 건수 */
    private Integer totalCount;

    /** 수집 시각 */
    private LocalDateTime collectedAt;
}
