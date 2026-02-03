package com.stock.webpage.mapper;

import com.stock.webpage.dto.BatchHistoryView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchHistoryMapper {

    List<BatchHistoryView> selectAllTimelineOrderByExecStartTimeDesc();

}

