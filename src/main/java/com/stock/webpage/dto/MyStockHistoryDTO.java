package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyStockHistoryDTO {

    private Long histId;
    private Long myStockId;

    private String userId;
    private String code;
    private String name;

    private String action;          // ADD / UPDATE / DELETE / RESTORE
    private String strategyName;

    private Double specialValue;
    private Double priceAtAdd;
    private Double targetPrice5;
    private Double targetPrice10;

    private String memo;
    private LocalDateTime createdAt;

    private String note;
}
