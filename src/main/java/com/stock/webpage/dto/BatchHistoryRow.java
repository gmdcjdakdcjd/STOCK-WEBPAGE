package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchHistoryRow implements BatchHistoryView {

    private String type;      // "IN" / "OUT"

    private Long histId;
    private Long jobId;
    private String jobName;
    private String jobInfo;

    private LocalDateTime execStartTime;
    private LocalDateTime execEndTime;

    private String execStatus;
    private String execMessage;

    private LocalDate execDate;
    private Long durationMs;
}
