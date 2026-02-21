//package com.stock.webpage.dto;
//
//import lombok.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class BatchOutHistoryDTO implements BatchHistoryView {
//
//    private Long histId;
//    private Long jobId;
//    private String jobName;
//    private String jobInfo;
//
//    private LocalDateTime execStartTime;
//    private LocalDateTime execEndTime;
//
//    private String execStatus;
//    private String execMessage;
//
//    private LocalDate execDate;
//    private Long durationMs;
//
//    // 가공용
//    private String type;
//
//    @Override
//    public String getType() {
//        return "OUT";
//    }
//}
