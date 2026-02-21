//package com.stock.webpage.service.impl;
//
//import com.stock.webpage.dto.BatchOutHistoryDTO;
//import com.stock.webpage.mapper.BatchOutHistoryMapper;
//import com.stock.webpage.service.BatchOutHistoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BatchOutHistoryServiceImpl
//        implements BatchOutHistoryService {
//
//    private final BatchOutHistoryMapper outMapper;
//
//    @Override
//    public List<BatchOutHistoryDTO> getAllOrderByExecStartTimeDesc() {
//        return outMapper.selectAllOrderByExecStartTimeDesc();
//    }
//}
