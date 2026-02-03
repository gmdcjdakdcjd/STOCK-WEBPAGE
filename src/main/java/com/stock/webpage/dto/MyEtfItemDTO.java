package com.stock.webpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyEtfItemDTO {

    private Long id;

    private String userId;
    private String code;
    private String name;

    private String etfName;
    private String etfDescription;

    private Double priceAtAdd;
    private Integer quantity;

    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String deletedYn;
    private LocalDateTime deletedAt;
}
