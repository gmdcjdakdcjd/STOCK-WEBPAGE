package com.stock.webpage.mapper;

import com.stock.webpage.dto.DailyPriceUsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DailyPriceUsMapper {

    // JPA: findAllByCodeOrderByDateDesc
    List<DailyPriceUsDTO> selectByCodeOrderByDateDesc(
            @Param("code") String code
    );

    // 확장용: 최신 N개
    List<DailyPriceUsDTO> selectTopByCodeOrderByDateDesc(
            @Param("code") String code,
            @Param("limit") int limit
    );

    // 가격 테이블 페이징 조회용
    List<DailyPriceUsDTO> selectPricePageByCode(
            @Param("code") String code,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
