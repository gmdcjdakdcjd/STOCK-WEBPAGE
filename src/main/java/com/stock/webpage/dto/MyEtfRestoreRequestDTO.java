package com.stock.webpage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyEtfRestoreRequestDTO {

    private String etfName;

    /** 복구할 history id 목록 */
    private List<Long> historyIds;
}
