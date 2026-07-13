package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 배치 작업 정보(설정 목록 및 실행 상태)를 나타내는 DTO 클래스입니다.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchJobDTO {
    private String type; // "IN" (수집) 또는 "OUT" (출력/분석)
    private Long jobId;
    private String jobName;
    private String jobInfo;
    private String scheduleGb;
    private String jobMonth;
    private String jobDay;
    private String jobWeek;
    private String jobHour;
    private String jobMin;
    private String actGb;
    private String lastExecInfo;
    private String nextExecInfo;
    private Integer isActive;
}
