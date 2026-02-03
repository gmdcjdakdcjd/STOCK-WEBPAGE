package com.stock.webpage.service;

import com.stock.webpage.dto.BatchInHistoryDTO;

import java.util.List;

public interface BatchInHistoryService {
    List<BatchInHistoryDTO> getAllOrderByExecStartTimeDesc();
}
