package com.stock.webpage.mapper;

import com.stock.webpage.dto.DailyPriceKrDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DailyPriceKrMapper {

    // JPA: findAllByCodeOrderByDateDesc
    List<DailyPriceKrDTO> selectByCodeOrderByDateDesc(
            @Param("code") String code
    );

    // (확장용) 최신 N개
    List<DailyPriceKrDTO> selectTopByCodeOrderByDateDesc(
            @Param("code") String code,
            @Param("limit") int limit
    );
}
