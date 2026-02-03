package com.stock.webpage.mapper;

import com.stock.webpage.dto.MarketIndicatorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockIndexMapper {

    // findByCodeOrderByDateAsc
    List<MarketIndicatorDTO> selectByCodeOrderByDateAsc(
            @Param("code") String code
    );
}
