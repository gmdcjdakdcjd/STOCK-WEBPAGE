package com.stock.webpage.mapper;

import com.stock.webpage.dto.BatchOutHistoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchOutHistoryMapper {

    List<BatchOutHistoryDTO> selectAllOrderByExecStartTimeDesc();
}