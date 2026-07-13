package com.stock.webpage.service;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import com.stock.webpage.dto.BatchJobDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.mapper.BatchHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageBatchHistoryService {

    private final BatchHistoryService batchHistoryService;
    private final BatchHistoryMapper batchHistoryMapper;

    // 날짜 목록 조회
    public PageResponseDTO<BatchDateGroupDTO> getHistoryDates(PageRequestDTO pageRequestDTO) {

        int page = pageRequestDTO.getPage();
        int size = pageRequestDTO.getSize();
        int offset = pageRequestDTO.getOffset();
        String jobName = pageRequestDTO.getJobName();

        List<BatchDateGroupDTO> list = batchHistoryService.getHistoryDates(offset, size, jobName);

        int total = batchHistoryService.getHistoryDateTotalCount(jobName); // count 쿼리 하나 추가 필요

        return PageResponseDTO.<BatchDateGroupDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    // 특정 날짜 상세 조회
    public PageResponseDTO<BatchHistoryView> getHistoryByDate(
            String date,
            PageRequestDTO pageRequestDTO) {

        int offset = pageRequestDTO.getOffset();
        int size = pageRequestDTO.getSize();

        List<BatchHistoryView> list = batchHistoryService.getHistoryByDate(date, offset, size);

        int total = batchHistoryService.getHistoryDetailTotalCount(date);

        return PageResponseDTO.<BatchHistoryView>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    /**
     * 전체 배치 작업(수집형 IN, 분석형 OUT)의 설정 정보 및 실시간 실행 상태 목록을 통합 조회합니다.
     */
    public List<BatchJobDTO> getAllBatchJobs() {
        List<BatchJobDTO> inJobs = batchHistoryMapper.selectBatchInJobList();
        List<BatchJobDTO> outJobs = batchHistoryMapper.selectBatchOutJobList();

        List<BatchJobDTO> allJobs = new ArrayList<>();
        if (inJobs != null)
            allJobs.addAll(inJobs);
        if (outJobs != null)
            allJobs.addAll(outJobs);
        return allJobs;
    }

    /**
     * 특정 배치 작업을 즉시 재처리(다시 기동)하도록 상태를 리셋하고 큐를 밀어넣습니다.
     * 
     * @param type  배치 유형 (IN / OUT)
     * @param jobId 배치 작업 일련번호
     */
    @Transactional
    public void triggerBatchJob(String type, Long jobId) {
        if ("IN".equalsIgnoreCase(type)) {
            // 1. BatchIn은 큐를 타지 않으므로, 현재 시각으로부터 2분 뒤로 스케줄 예약 시간을 설정하여 act_gb='N' 상태로 구동을 유도합니다.
            java.time.LocalTime targetTime = java.time.LocalTime.now().plusMinutes(2);
            String targetHour = String.format("%02d", targetTime.getHour());
            String targetMin = String.format("%02d", targetTime.getMinute());
            batchHistoryMapper.updateBatchInTriggerTime(jobId, targetHour, targetMin);
        } else if ("OUT".equalsIgnoreCase(type)) {
            // 2. BatchOut은 대기상태(N) 갱신과 더불어 대기열(stock_job_queue)에 인서트하여 즉각 실행을 명령합니다.
            List<BatchJobDTO> outJobs = batchHistoryMapper.selectBatchOutJobList();
            BatchJobDTO targetJob = outJobs.stream()
                    .filter(j -> j.getJobId().equals(jobId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 BatchOut 작업입니다."));

            batchHistoryMapper.updateBatchOutActGb(jobId, "N");
            batchHistoryMapper.insertStockJobQueue(targetJob.getJobName(), "W", jobId);
        } else {
            throw new IllegalArgumentException("올바르지 않은 배치 유형 구분자입니다.");
        }
    }

    /**
     * 특정 배치 작업명(jobName) 기준 최근 실행 이력 로그 목록을 최대 N건 한도로 조회합니다.
     */
    public List<BatchHistoryView> getHistoryByJobName(String jobName, int size) {
        return batchHistoryMapper.selectHistoryByJobName(jobName, size);
    }

    /**
     * 특정 배치 작업의 실행 주기 스케줄 설정을 실시간 변경(수정)합니다.
     */
    @Transactional
    public void updateBatchJobSchedule(String type, Long jobId, BatchJobDTO scheduleDTO) {
        if ("IN".equalsIgnoreCase(type)) {
            batchHistoryMapper.updateBatchInSchedule(
                jobId,
                scheduleDTO.getScheduleGb(),
                scheduleDTO.getJobHour(),
                scheduleDTO.getJobMin(),
                scheduleDTO.getJobWeek(),
                scheduleDTO.getJobDay(),
                scheduleDTO.getJobMonth()
            );
        } else if ("OUT".equalsIgnoreCase(type)) {
            batchHistoryMapper.updateBatchOutSchedule(
                jobId,
                scheduleDTO.getScheduleGb(),
                scheduleDTO.getJobHour(),
                scheduleDTO.getJobMin(),
                scheduleDTO.getJobWeek(),
                scheduleDTO.getJobDay(),
                scheduleDTO.getJobMonth()
            );
        } else {
            throw new IllegalArgumentException("올바르지 않은 배치 유형 구분자입니다.");
        }
    }
}