package com.stock.webpage.service;

import com.stock.webpage.dto.InvestorFlowDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;

public interface InvestorTrendService {

    // 상단 박스용 최신 1건
    InvestorFlowDTO getLatestInvestorTrend();

    // 페이징 리스트
    PageResponseDTO<InvestorFlowDTO> getInvestorTrendList(PageRequestDTO pageRequestDTO);
}