package com.stock.webpage.mapper;

import com.stock.webpage.dto.StrategyDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StrategyDetailMapper {

    // JPA:
    // findByActionAndSignalDateOrderBySpecialValueAsc
    List<StrategyDetailDTO> selectByActionAndSignalDateOrderBySpecialValueAsc(
            @Param("action") String action,
            @Param("signalDate") LocalDate signalDate
    );

    // JPA:
    // findByActionOrderBySignalDateDesc
    List<StrategyDetailDTO> selectByActionOrderBySignalDateDesc(
            @Param("action") String action
    );

    // JPA:
    // findByKeyword (DTO projection)
    List<StrategyDetailDTO> selectByKeyword(
            @Param("keyword") String keyword
    );

    // 키워드 검색 (페이징)
    List<StrategyDetailDTO> selectByKeywordPaged(
            @Param("keyword") String keyword,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
