package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDateGroupDTO {

    private LocalDate execDate;
    private Long totalCount;

    // 해당 날짜에 실행 중 실패(FAIL)가 기록된 배치 작업의 총 건수입니다.
    private Long failCount;
}