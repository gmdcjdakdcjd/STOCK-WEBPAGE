package com.stock.webpage.service;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.StrategyResultDTO;

import java.time.LocalDate;

public interface StrategyResultService {

    PageResponseDTO<StrategyResultDTO> listKR(
            PageRequestDTO dto,
            String strategy,
            LocalDate regDate
    );

    PageResponseDTO<StrategyResultDTO> listUS(
            PageRequestDTO dto,
            String strategy,
            LocalDate regDate
    );
}
