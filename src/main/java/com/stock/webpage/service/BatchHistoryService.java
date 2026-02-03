package com.stock.webpage.service;

import com.stock.webpage.dto.BatchHistoryView;

import java.util.List;

public interface BatchHistoryService {

    List<BatchHistoryView> getAllTimelineOrderByExecStartTimeDesc();

}
