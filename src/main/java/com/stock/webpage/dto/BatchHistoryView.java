package com.stock.webpage.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistoryView {

    private LocalDate execDate;
    private String type;

    private Long histId;
    private Long jobId;
    private String jobName;
    private String execStatus;
    private String execMessage;

    private LocalDateTime execStartTime;
    private LocalDateTime execEndTime;
    private Long durationMs;
}