package com.stock.webpage.mapper;


import com.stock.webpage.dto.BatchInHistoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchInHistoryMapper {

    List<BatchInHistoryDTO> selectAllOrderByExecStartTimeDesc();
}