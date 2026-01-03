package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyStockDTO {

    private Long id;
    private String userId;
    private String code;
    private String name;
    private String strategyName;
    private Double specialValue;
    private Double priceAtAdd;
    private Double targetPrice5;
    private Double targetPrice10;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deletedYn;
    private LocalDateTime deletedAt;

    // 조회 전용 (조인/서비스에서 채움)
    private Double currentPrice;
}
