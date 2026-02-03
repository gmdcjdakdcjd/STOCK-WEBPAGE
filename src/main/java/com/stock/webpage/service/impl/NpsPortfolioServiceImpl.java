package com.stock.webpage.service.impl;

import com.stock.webpage.dto.NpsPortfolioDTO;
import com.stock.webpage.mapper.NpsPortfolioMapper;
import com.stock.webpage.service.NpsPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NpsPortfolioServiceImpl implements NpsPortfolioService {

    private final NpsPortfolioMapper portfolioMapper;

    @Override
    public List<NpsPortfolioDTO> getLatestSummary(String institutionCode) {
        return portfolioMapper.selectLatestSummary(institutionCode);
    }
}
