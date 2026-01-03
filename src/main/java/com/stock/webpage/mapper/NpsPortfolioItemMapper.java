package com.stock.webpage.mapper;

import com.stock.webpage.dto.NpsPortfolioItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NpsPortfolioItemMapper {

    List<NpsPortfolioItemDTO> selectItemList(
            @Param("institutionCode") String institutionCode,
            @Param("asset") String asset,
            @Param("market") String market,
            @Param("q") String q
    );

    List<String> selectAutocompleteNames(
            @Param("institutionCode") String institutionCode,
            @Param("asset") String asset,
            @Param("market") String market,
            @Param("q") String q
    );
}