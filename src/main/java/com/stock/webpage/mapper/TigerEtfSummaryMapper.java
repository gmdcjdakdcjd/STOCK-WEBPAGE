package com.stock.webpage.mapper;

import com.stock.webpage.dto.TigerEtfSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TigerEtfSummaryMapper {

    // 전체 ETF
    List<TigerEtfSummaryDTO> selectAll();

    // ETF명 / ETF코드 검색
    List<TigerEtfSummaryDTO> selectByEtfNameOrEtfId(
            @Param("keyword") String keyword
    );

    // ETF ID 목록으로 조회 (종목 → ETF)
    List<TigerEtfSummaryDTO> selectByEtfIdIn(
            @Param("etfIds") List<String> etfIds
    );
}
