package com.stock.webpage.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NpsPortfolioItemDTO {

    /** 기관 코드 (NPS) */
    private String institutionCode;

    /** 기준일 */
    private LocalDate baseDate;

    /** 자산 유형 (STOCK | ETF | BOND) */
    private String assetType;

    /** 시장 (KR | US | GLOBAL) */
    private String market;

    /** 순위 */
    private Integer rankNo;

    /** 종목명 / 발행기관명 / 채권명 */
    private String name;

    /** 자산 세부 유형 (TREASURIES, CORPORATE 등) */
    private String assetSubType;

    /** 비중 (%) */
    private BigDecimal weightPct;

    /** 지분율 (%) - 주식만 사용 */
    private BigDecimal ownershipPct;

    /** 평가액 (억 원) */
    private BigDecimal evalAmount100m;

    /** 수집 시각 */
    private LocalDateTime collectedAt;

    /* =========================
   화면 표시용 필드
   ========================= */

    /** 평가액 표시용 (억, 콤마, 소수점 제거) */
    private String evalAmountDisplay;
}
