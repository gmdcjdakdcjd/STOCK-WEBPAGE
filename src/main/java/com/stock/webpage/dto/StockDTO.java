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

    private List<PriceDTO> priceList;
}
