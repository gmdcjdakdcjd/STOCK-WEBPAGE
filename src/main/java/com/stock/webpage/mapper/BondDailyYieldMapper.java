package com.stock.webpage.mapper;

import com.stock.webpage.dto.BondDailyYieldDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BondDailyYieldMapper {

    List<BondDailyYieldDTO> selectByCodeOrderByDateAsc(@Param("code") String code);
}