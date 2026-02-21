package com.stock.webpage.service.impl;

import com.stock.webpage.dto.InvestorFlowDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.mapper.InvestorTrendMapper;
import com.stock.webpage.service.InvestorTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestorTrendServiceImpl implements InvestorTrendService {

    private final InvestorTrendMapper investorTrendMapper;

    // 최신 1건
    @Override
    public InvestorFlowDTO getLatestInvestorTrend() {
        return investorTrendMapper.selectLatestInvestorTrend();
    }

    // 페이징 리스트
    @Override
    public PageResponseDTO<InvestorFlowDTO> getInvestorTrendList(PageRequestDTO pageRequestDTO) {

        int total = investorTrendMapper.selectInvestorTrendCount();

        List<InvestorFlowDTO> list =
                investorTrendMapper.selectInvestorTrendList(pageRequestDTO);

        return PageResponseDTO.<InvestorFlowDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }
}