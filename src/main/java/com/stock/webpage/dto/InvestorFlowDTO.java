package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorFlowDTO {

    private Long id;

    private LocalDateTime baseDate;

    private Long individual;      // 개인
    private Long foreigner;       // 외국인
    private Long institutional;   // 기관

    private Long finInvest;       // 금융투자
    private Long insurance;       // 보험
    private Long invTrust;        // 투신
    private Long bank;            // 은행
    private Long etcFin;          // 기타금융
    private Long pension;         // 연기금
    private Long etcCorp;         // 기타법인

    private LocalDateTime lastUpdate;
}