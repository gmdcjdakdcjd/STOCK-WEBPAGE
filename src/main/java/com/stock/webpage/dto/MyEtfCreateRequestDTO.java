package com.stock.webpage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// ETF 생성 요청 전체
@Getter
@Setter
public class MyEtfCreateRequestDTO {

    private String etfName;
    private String etfDescription;
    private List<MyEtfItemRequestDTO> items;
}
