package com.stock.webpage.api;

import com.stock.webpage.dto.*;
import com.stock.webpage.service.ManageBatchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ManageBatchHistoryApiController {

    private final ManageBatchHistoryService historyService;

    /*
     * =========================
     * 날짜 목록 조회
     * =========================
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history/dates")
    public PageResponseDTO<BatchDateGroupDTO> getHistoryDates(
            PageRequestDTO pageRequestDTO) {
        return historyService.getHistoryDates(pageRequestDTO);
    }

    /*
     * =========================
     * 특정 날짜 상세 조회
     * =========================
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history/{date}")
    public PageResponseDTO<BatchHistoryView> getHistoryByDate(
            @PathVariable String date,
            PageRequestDTO pageRequestDTO) {
        return historyService.getHistoryByDate(date, pageRequestDTO);
    }

    /*
     * 배치 작업 목록 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/jobs")
    public ResponseEntity<?> getBatchJobs() {
        try {
            return ResponseEntity.ok(historyService.getAllBatchJobs());
        } catch (Exception e) {
            log.error("배치 작업 목록 조회 실패: ", e);
            return ResponseEntity.internalServerError().body(java.util.Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * 배치 작업 재처리 트리거
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch/jobs/{type}/{jobId}/trigger")
    public ResponseEntity<?> triggerBatchJob(
            @PathVariable String type,
            @PathVariable Long jobId) {
        log.info("관리자 권한 배치 재처리 API 호출 - 유형: {}, Job ID: {}", type, jobId);
        try {
            historyService.triggerBatchJob(type, jobId);
            return ResponseEntity.ok(java.util.Map.of("result", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        } catch (Exception e) {
            log.error("배치 재처리 실패: ", e);
            return ResponseEntity.internalServerError().body(java.util.Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
        특정 배치 Job명 기준 최근 이력 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history/job/{jobName}")
    public ResponseEntity<?> getHistoryByJob(
            @PathVariable String jobName,
            @RequestParam(defaultValue = "50") int size
    ) {
        try {
            return ResponseEntity.ok(historyService.getHistoryByJobName(jobName, size));
        } catch (Exception e) {
            log.error("특정 Job 이력 조회 실패: ", e);
            return ResponseEntity.internalServerError().body(java.util.Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()
            ));
        }
    }

    /*
        배치 스케줄러 정보 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch/jobs/{type}/{jobId}/schedule")
    public ResponseEntity<?> updateBatchSchedule(
            @PathVariable String type,
            @PathVariable Long jobId,
            @RequestBody BatchJobDTO scheduleDTO
    ) {
        try {
            historyService.updateBatchJobSchedule(type, jobId, scheduleDTO);
            return ResponseEntity.ok(java.util.Map.of("result", "OK"));
        } catch (Exception e) {
            log.error("배치 스케줄 수정 실패: ", e);
            return ResponseEntity.internalServerError().body(java.util.Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()
            ));
        }
    }
}