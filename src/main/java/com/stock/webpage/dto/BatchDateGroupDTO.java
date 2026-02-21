package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class BatchDateGroupDTO {

    private LocalDate execDate;
    private Long totalCount;
}