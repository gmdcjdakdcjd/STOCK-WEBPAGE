package com.stock.webpage.service;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;

import java.util.List;

public interface BatchHistoryService {

    // 날짜 목록 조회 (group by exec_date)
    List<BatchDateGroupDTO> getHistoryDates(int offset, int size);
    List<BatchDateGroupDTO> getHistoryDates(int offset, int size, String jobName);

    // 날짜 목록 전체 개수 (distinct exec_date count)
    int getHistoryDateTotalCount();
    int getHistoryDateTotalCount(String jobName);

    // 특정 날짜 상세 조회
    List<BatchHistoryView> getHistoryByDate(String date, int offset, int size);

    // 특정 날짜 전체 개수
    int getHistoryDetailTotalCount(String date);
}