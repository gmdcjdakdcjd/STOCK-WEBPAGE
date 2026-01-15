package com.stock.webpage.api;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.BatchDateGroupDTO;
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
       배치 이력 조회 (관리자)
       ========================= */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history")
    public PageResponseDTO<BatchDateGroupDTO> getBatchHistory(
            PageRequestDTO pageRequestDTO
    ) {
        return historyService.getGroupedHistory(pageRequestDTO);
    }
}
