package com.stock.webpage.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyEtfDetailResponseDTO {

    private String etfName;
    private String etfDescription;
    private MyEtfDetailSummaryDTO summary;
    private List<MyEtfItemViewDTO> itemList;
}
