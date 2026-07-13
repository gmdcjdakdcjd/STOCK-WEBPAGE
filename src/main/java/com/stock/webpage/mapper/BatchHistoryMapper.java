package com.stock.webpage.mapper;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import com.stock.webpage.dto.BatchJobDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BatchHistoryMapper {

    List<BatchDateGroupDTO> selectHistoryDateList(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("jobName") String jobName
    );

    int selectHistoryDateTotalCount(@Param("jobName") String jobName);

    List<BatchHistoryView> selectHistoryByDate(
            @Param("date") String date,
            @Param("offset") int offset,
            @Param("size") int size
    );

    int selectHistoryDetailTotalCount(
            @Param("date") String date
    );

    // 배치 작업 목록 조회 및 재처리 제어용 추가 매퍼
    List<BatchJobDTO> selectBatchInJobList();

    List<BatchJobDTO> selectBatchOutJobList();

    int updateBatchInActGb(@Param("jobId") Long jobId, @Param("actGb") String actGb);

    int updateBatchInTriggerTime(
            @Param("jobId") Long jobId,
            @Param("jobHour") String jobHour,
            @Param("jobMin") String jobMin
    );

    int updateBatchOutActGb(@Param("jobId") Long jobId, @Param("actGb") String actGb);

    int insertStockJobQueue(
            @Param("jobCode") String jobCode,
            @Param("status") String status,
            @Param("batchOutId") Long batchOutId
    );

    // 특정 배치 Job명 기준 최근 이력 목록 조회
    List<BatchHistoryView> selectHistoryByJobName(
            @Param("jobName") String jobName,
            @Param("size") int size
    );

    int updateBatchInSchedule(
            @Param("jobId") Long jobId,
            @Param("scheduleGb") String scheduleGb,
            @Param("jobHour") String jobHour,
            @Param("jobMin") String jobMin,
            @Param("jobWeek") String jobWeek,
            @Param("jobDay") String jobDay,
            @Param("jobMonth") String jobMonth
    );

    int updateBatchOutSchedule(
            @Param("jobId") Long jobId,
            @Param("scheduleGb") String scheduleGb,
            @Param("jobHour") String jobHour,
            @Param("jobMin") String jobMin,
            @Param("jobWeek") String jobWeek,
            @Param("jobDay") String jobDay,
            @Param("jobMonth") String jobMonth
    );
}