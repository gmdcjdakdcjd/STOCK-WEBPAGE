package com.stock.webpage.service;

import com.stock.webpage.dto.NpsPortfolioDTO;

import java.util.List;

public interface NpsPortfolioService {

    List<NpsPortfolioDTO> getLatestSummary(String institutionCode);
}
