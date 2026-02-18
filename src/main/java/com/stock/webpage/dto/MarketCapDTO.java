package com.stock.webpage.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketCapDTO {

    private String code;
    private String name;
    private Long listedStockCount;   // 상장주식수
    private Long currentPrice;       // 현재가
    private Long marketCap;          // 시가총액
    private Integer ranking;

}
