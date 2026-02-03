package com.stock.webpage.mapper;

import com.stock.webpage.dto.MarketIndicatorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeMapper {

    // findByCodeOrderByDateAsc
    List<MarketIndicatorDTO> selectByCodeOrderByDateAsc(
            @Param("code") String code
    );

    // findLatestValue
    Double selectLatestCloseByCode(
            @Param("code") String code
    );
}