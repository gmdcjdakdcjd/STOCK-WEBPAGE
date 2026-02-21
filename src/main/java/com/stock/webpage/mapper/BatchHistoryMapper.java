package com.stock.webpage.mapper;

import com.stock.webpage.dto.BatchDateGroupDTO;
import com.stock.webpage.dto.BatchHistoryView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BatchHistoryMapper {

    // 1️ 날짜 목록 조회 (group by exec_date + limit)
    List<BatchDateGroupDTO> selectHistoryDateList(
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 2️ 날짜 목록 전체 개수 (distinct exec_date count)
    int selectHistoryDateTotalCount();

    // 3️ 특정 날짜 상세 조회 (limit)
    List<BatchHistoryView> selectHistoryByDate(
            @Param("date") String date,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 4️ 특정 날짜 전체 개수
    int selectHistoryDetailTotalCount(
            @Param("date") String date
    );
}