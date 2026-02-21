package com.stock.webpage.service;

import com.stock.webpage.dto.DepositTrendBoxDTO;
import com.stock.webpage.dto.DepositTrendDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;

public interface DepositTrendService {

    // 상단 박스 (오늘/어제 비교)
    DepositTrendBoxDTO getDepositTrendBox();

    // 페이징 리스트
    PageResponseDTO<DepositTrendDTO> getDepositTrendList(PageRequestDTO pageRequestDTO);
}