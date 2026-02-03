package com.stock.webpage.mapper;

import com.stock.webpage.dto.CompanyInfoKrDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyInfoKrMapper {

    // 이름 포함 검색
    List<CompanyInfoKrDTO> selectByNameContaining(
            @Param("name") String name
    );

    // 자동완성 (이름 or 코드, 상위 N개)
    List<CompanyInfoKrDTO> selectTopByNameOrCodeContaining(
            @Param("keyword") String keyword,
            @Param("limit") int limit
    );

    // 코드 단건 조회 (상세 화면용)
    CompanyInfoKrDTO selectByCode(
            @Param("code") String code
    );
}
