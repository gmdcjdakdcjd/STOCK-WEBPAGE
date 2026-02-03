package com.stock.webpage.mapper;

import com.stock.webpage.dto.MyStockHistoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyStockHistoryMapper {

    // 특정 종목 히스토리 (최근순)
    List<MyStockHistoryDTO> selectByMyStockId(Long myStockId);

    // 유저 전체 히스토리 (최근순)
    List<MyStockHistoryDTO> selectByUserId(String userId);

    // 히스토리 저장
    int insertHistory(MyStockHistoryDTO dto);
}
