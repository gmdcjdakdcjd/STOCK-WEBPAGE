package com.stock.webpage.service;

import com.stock.webpage.dto.StrategyDetailDTO;

import java.time.LocalDate;
import java.util.List;

public interface StrategyDetailService {

    List<StrategyDetailDTO> getLatestOrToday(String strategyName, String today);

    List<StrategyDetailDTO> getDetail(String strategy, LocalDate date);

    List<StrategyDetailDTO> searchDetail(String keyword);
}
