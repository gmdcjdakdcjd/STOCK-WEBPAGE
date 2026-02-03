package com.stock.webpage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyEtfEditRequestDTO {

    /** ETF 이름 (식별자) */
    private String etfName;

    /** 편집 대상 종목 목록 */
    private List<MyEtfEditItemDTO> items;

    /** ETF 설명 (메타 정보, 수정 가능) */
    private String etfDescription;
}
