package com.stock.webpage.service;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.ProgramTrendDTO;

public interface ProgramTrendService {

    // 최신 1건 (상단 박스용)
    ProgramTrendDTO getLatestProgramTrend();

    // 페이징 리스트
    PageResponseDTO<ProgramTrendDTO> getProgramTrendList(PageRequestDTO pageRequestDTO);
}