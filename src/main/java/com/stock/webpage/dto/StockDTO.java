package com.stock.webpage.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StockDTO {

    private String code;
    private String name;
    private String marketType; // "KR" | "US"

    // 둘 중 하나만 채워짐
    private CompanyInfoKrDTO companyInfoKr;
    private CompanyInfoUsDTO companyInfoUs;

    // 차트 표시용 1년치 가격 데이터 (최대 365개)
    private List<PriceDTO> chartPriceList;

    // 가격 테이블 표시용 최근 가격 데이터 (최대 20개)
    private List<PriceDTO> priceList;
}
