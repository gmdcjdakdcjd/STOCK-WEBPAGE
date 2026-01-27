package com.stock.webpage.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyInfoKrDTO implements CompanyInfoDTO{

    private String code;          // 종목코드
    private String name;          // 종목명
    private String marketType;    // KOSPI / KOSDAQ
    private String securityType;  // 보통주 / 우선주 등
    private String stockType;     // 주식 유형
    private String stdCode;       // 표준코드
    private LocalDate lastUpdate; // 마지막 업데이트

    private Double price;
}
