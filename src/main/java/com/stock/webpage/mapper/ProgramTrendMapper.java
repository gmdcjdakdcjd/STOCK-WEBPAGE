package com.stock.webpage.mapper;

import com.stock.webpage.dto.ProgramTrendDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProgramTrendMapper {

    // 상단 박스용 (최신 2건)
    List<ProgramTrendDTO> selectLatestTwoProgramTrend();

    // 최신 1건만 필요할 경우
    ProgramTrendDTO selectLatestProgramTrend();

    // 페이징 리스트
    List<ProgramTrendDTO> selectProgramTrendList(PageRequestDTO pageRequestDTO);

    // 전체 개수
    int selectProgramTrendCount();
}