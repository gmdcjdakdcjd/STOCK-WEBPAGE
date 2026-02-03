package com.stock.webpage.service.impl;

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

    @Override
    public List<BatchHistoryView> getAllTimelineOrderByExecStartTimeDesc() {
        return batchHistoryMapper.selectAllTimelineOrderByExecStartTimeDesc();
    }
}
