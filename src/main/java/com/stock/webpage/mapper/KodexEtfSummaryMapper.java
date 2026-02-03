package com.stock.webpage.mapper;

import com.stock.webpage.dto.KodexEtfSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KodexEtfSummaryMapper {

    // 전체 ETF
    List<KodexEtfSummaryDTO> selectAll();

    // ETF명 / ETF코드 검색
    List<KodexEtfSummaryDTO> selectByEtfNameOrEtfId(
            @Param("keyword") String keyword
    );

    // ETF ID 목록으로 조회 (종목 → ETF)
    List<KodexEtfSummaryDTO> selectByEtfIdIn(
            @Param("etfIds") List<String> etfIds
    );
}
