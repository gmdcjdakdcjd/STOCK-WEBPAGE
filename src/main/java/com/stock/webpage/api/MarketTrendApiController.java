package com.stock.webpage.api;

import com.stock.webpage.dto.*;
import com.stock.webpage.service.DepositTrendService;
import com.stock.webpage.service.InvestorTrendService;
import com.stock.webpage.service.ProgramTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market-trend")
@RequiredArgsConstructor
public class MarketTrendApiController {

    private final DepositTrendService depositTrendService;
    private final InvestorTrendService investorTrendService;
    private final ProgramTrendService programTrendService;

    // =========================
    // 상단 요약 (Summary / Latest)
    // =========================

    @GetMapping("/deposit/summary")
    public DepositTrendBoxDTO getDepositSummary() {
        return depositTrendService.getDepositTrendBox();
    }

    @GetMapping("/investor/latest")
    public InvestorFlowDTO getInvestorLatest() {
        return investorTrendService.getLatestInvestorTrend();
    }

    @GetMapping("/program/latest")
    public ProgramTrendDTO getProgramLatest() {
        return programTrendService.getLatestProgramTrend();
    }

    // =========================
    // 하단 페이징 리스트
    // =========================

    @GetMapping("/deposit")
    public PageResponseDTO<DepositTrendDTO> getDepositList(PageRequestDTO requestDTO) {
        return depositTrendService.getDepositTrendList(requestDTO);
    }

    @GetMapping("/investor")
    public PageResponseDTO<InvestorFlowDTO> getInvestorList(PageRequestDTO requestDTO) {
        return investorTrendService.getInvestorTrendList(requestDTO);
    }

    @GetMapping("/program")
    public PageResponseDTO<ProgramTrendDTO> getProgramList(PageRequestDTO requestDTO) {
        return programTrendService.getProgramTrendList(requestDTO);
    }
}