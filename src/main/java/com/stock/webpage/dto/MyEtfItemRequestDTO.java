package com.stock.webpage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyEtfItemRequestDTO {

    private String etfName;           // ETF 이름
    private String etfDescription;    // ETF 설명

    private String code;
    private String name;
    private Integer quantity;
    private String memo;
}
