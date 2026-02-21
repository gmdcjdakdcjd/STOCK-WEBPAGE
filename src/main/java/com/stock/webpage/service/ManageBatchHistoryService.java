package com.stock.webpage.service;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageBatchHistoryService {

    private final BatchHistoryService batchHistoryService;

    // 날짜 목록 조회
    public PageResponseDTO<BatchDateGroupDTO> getHistoryDates(PageRequestDTO pageRequestDTO) {

        int page = pageRequestDTO.getPage();
        int size = pageRequestDTO.getSize();
        int offset = pageRequestDTO.getOffset();

        List<BatchDateGroupDTO> list =
                batchHistoryService.getHistoryDates(offset, size);

        int total =
                batchHistoryService.getHistoryDateTotalCount(); // count 쿼리 하나 추가 필요

        return PageResponseDTO.<BatchDateGroupDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    //  특정 날짜 상세 조회
    public PageResponseDTO<BatchHistoryView> getHistoryByDate(
            String date,
            PageRequestDTO pageRequestDTO
    ) {

        int offset = pageRequestDTO.getOffset();
        int size = pageRequestDTO.getSize();

        List<BatchHistoryView> list =
                batchHistoryService.getHistoryByDate(date, offset, size);

        int total =
                batchHistoryService.getHistoryDetailTotalCount(date);

        return PageResponseDTO.<BatchHistoryView>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }
}