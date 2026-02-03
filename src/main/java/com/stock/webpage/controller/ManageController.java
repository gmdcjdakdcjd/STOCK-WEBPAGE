package com.stock.webpage.controller;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.service.ManageBatchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManageController {

    private final ManageBatchHistoryService historyService;

    /* =========================
       배치 이력 관리 화면 (관리자)
       ========================= */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/batch/history")
    public String viewBatchHistory(
            PageRequestDTO pageRequestDTO,
            Model model
    ) {
        model.addAttribute(
                "result",
                historyService.getGroupedHistory(pageRequestDTO)
        );
        return "manage/batch/history";
    }

}
