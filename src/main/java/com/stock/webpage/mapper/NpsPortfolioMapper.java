package com.stock.webpage.mapper;

import com.stock.webpage.dto.NpsPortfolioDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NpsPortfolioMapper {

    List<NpsPortfolioDTO> selectLatestSummary(
            @Param("institutionCode") String institutionCode
    );
}