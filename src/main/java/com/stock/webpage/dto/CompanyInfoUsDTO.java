package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfoUsDTO implements CompanyInfoDTO {

    private String code;
    private String name;

    private String sector;
    private String industry;
    private String market;

    private Long cik;
    private LocalDate lastUpdate;

    private Double price;
}
