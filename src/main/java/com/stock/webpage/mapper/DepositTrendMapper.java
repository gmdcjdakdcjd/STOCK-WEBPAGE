package com.stock.webpage.mapper;

import com.stock.webpage.dto.DepositTrendDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepositTrendMapper {

    // 상단 박스용 (오늘/어제 비교용 2건)
    List<DepositTrendDTO> selectLatestTwoDepositTrend();

    // 최신 1건만 필요할 경우
    DepositTrendDTO selectLatestDepositTrend();

    // 페이징 리스트
    List<DepositTrendDTO> selectDepositTrendList(PageRequestDTO pageRequestDTO);

    // 전체 개수 (페이징용)
    int selectDepositTrendCount();
}