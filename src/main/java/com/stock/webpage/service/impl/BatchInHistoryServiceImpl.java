package com.stock.webpage.service.impl;

import com.stock.webpage.dto.BatchInHistoryDTO;
import com.stock.webpage.mapper.BatchInHistoryMapper;
import com.stock.webpage.service.BatchInHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchInHistoryServiceImpl
        implements BatchInHistoryService {

    private final BatchInHistoryMapper inMapper;

    @Override
    public List<BatchInHistoryDTO> getAllOrderByExecStartTimeDesc() {
        return inMapper.selectAllOrderByExecStartTimeDesc();
    }
}
