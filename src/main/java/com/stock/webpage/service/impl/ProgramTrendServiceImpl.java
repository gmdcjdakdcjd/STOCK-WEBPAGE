package com.stock.webpage.service.impl;

import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.dto.ProgramTrendDTO;
import com.stock.webpage.mapper.ProgramTrendMapper;
import com.stock.webpage.service.ProgramTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramTrendServiceImpl implements ProgramTrendService {

    private final ProgramTrendMapper programTrendMapper;

    // 최신 1건
    @Override
    public ProgramTrendDTO getLatestProgramTrend() {
        return programTrendMapper.selectLatestProgramTrend();
    }

    // 페이징 리스트
    @Override
    public PageResponseDTO<ProgramTrendDTO> getProgramTrendList(PageRequestDTO pageRequestDTO) {

        int total = programTrendMapper.selectProgramTrendCount();

        List<ProgramTrendDTO> list =
                programTrendMapper.selectProgramTrendList(pageRequestDTO);

        return PageResponseDTO.<ProgramTrendDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }
}