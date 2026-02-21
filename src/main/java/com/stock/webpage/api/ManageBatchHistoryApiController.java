package com.stock.webpage.api;

import com.stock.webpage.dto.*;
import com.stock.webpage.service.ManageBatchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage")
public class ManageBatchHistoryApiController {

    private final ManageBatchHistoryService historyService;

    /* =========================
        날짜 목록 조회
       ========================= */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history/dates")
    public PageResponseDTO<BatchDateGroupDTO> getHistoryDates(
            PageRequestDTO pageRequestDTO
    ) {
        return historyService.getHistoryDates(pageRequestDTO);
    }

    /* =========================
        특정 날짜 상세 조회
       ========================= */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history/{date}")
    public PageResponseDTO<BatchHistoryView> getHistoryByDate(
            @PathVariable String date,
            PageRequestDTO pageRequestDTO
    ) {
        return historyService.getHistoryByDate(date, pageRequestDTO);
    }
}