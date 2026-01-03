package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyEtfItemHistoryDTO {

    private Long histId;
    private Long etfItemId;

    private String userId;

    private String code;
    private String name;

    private String etfName;
    private String etfDescription;

    private Double priceAtAdd;
    private Integer quantity;

    private String memo;

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;

    private String restoredYn;
    private LocalDateTime restoredAt;
}
