package com.stock.webpage.service;

import com.stock.webpage.dto.BatchOutHistoryDTO;

import java.util.List;

public interface BatchOutHistoryService {

    List<BatchOutHistoryDTO> getAllOrderByExecStartTimeDesc();
}