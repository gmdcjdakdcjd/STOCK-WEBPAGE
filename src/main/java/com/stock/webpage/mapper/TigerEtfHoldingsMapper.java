package com.stock.webpage.mapper;

import com.stock.webpage.dto.TigerEtfHoldingsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TigerEtfHoldingsMapper {

    // ETF → 종목 (모달)
    List<TigerEtfHoldingsDTO> selectHoldingsByEtfIdOrderByWeight(
            @Param("etfId") String etfId
    );

    // 종목명 OR 종목코드 → ETF ID 목록
    List<String> selectEtfIdsByKeyword(
            @Param("keyword") String keyword
    );
}

