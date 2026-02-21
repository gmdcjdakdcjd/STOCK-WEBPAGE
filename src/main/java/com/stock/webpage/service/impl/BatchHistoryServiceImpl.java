package com.stock.webpage.service.impl;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import com.stock.webpage.mapper.BatchHistoryMapper;
import com.stock.webpage.service.BatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchHistoryServiceImpl implements BatchHistoryService {

    private final BatchHistoryMapper batchHistoryMapper;

    // 날짜 목록 조회
    @Override
    public List<BatchDateGroupDTO> getHistoryDates(int offset, int size) {
        return batchHistoryMapper.selectHistoryDateList(offset, size);
    }

    // 날짜 목록 총 개수
    @Override
    public int getHistoryDateTotalCount() {
        return batchHistoryMapper.selectHistoryDateTotalCount();
    }

    // 특정 날짜 상세 조회
    @Override
    public List<BatchHistoryView> getHistoryByDate(String date, int offset, int size) {
        return batchHistoryMapper.selectHistoryByDate(date, offset, size);
    }

    // 특정 날짜 총 개수
    @Override
    public int getHistoryDetailTotalCount(String date) {
        return batchHistoryMapper.selectHistoryDetailTotalCount(date);
    }
}