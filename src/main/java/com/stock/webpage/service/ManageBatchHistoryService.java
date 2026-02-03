package com.stock.webpage.service;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageBatchHistoryService {

//    private final BatchInHistoryService inService;
//    private final BatchOutHistoryService outService;
    private final BatchHistoryService batchHistoryService;

    public PageResponseDTO<BatchDateGroupDTO> getGroupedHistory(PageRequestDTO pageRequestDTO) {

        // IN + OUT 이력 수집
//        List<BatchHistoryView> all = new ArrayList<>();
//        all.addAll(inService.getAllOrderByExecStartTimeDesc());
//        all.addAll(outService.getAllOrderByExecStartTimeDesc());
        List<BatchHistoryView> all = batchHistoryService.getAllTimelineOrderByExecStartTimeDesc();


        // 날짜 기준 그룹화 + 정렬
        List<BatchDateGroupDTO> grouped = all.stream()
                .collect(Collectors.groupingBy(BatchHistoryView::getExecDate))
                .entrySet().stream()
                .sorted(
                        Map.Entry.<LocalDate, List<BatchHistoryView>>
                                comparingByKey().reversed()
                )
                .map(e -> new BatchDateGroupDTO(
                        e.getKey(),
                        e.getKey().toString().replace("-", ""),
                        e.getValue()
                ))
                .toList();

        // 여기서 페이징
        int total = grouped.size();
        int start = pageRequestDTO.getOffset();
        int end = Math.min(start + pageRequestDTO.getSize(), total);

        List<BatchDateGroupDTO> pageList =
                start >= total ? List.of() : grouped.subList(start, end);

        // PageResponseDTO 반환
        return PageResponseDTO.<BatchDateGroupDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(pageList)
                .total(total)
                .build();
    }
}
