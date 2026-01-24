package com.stock.webpage.mapper;

import com.stock.webpage.dto.MyEtfItemDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyEtfItemMapper {

    /* =========================
       ETF 요약 (기존)
       ========================= */
    List<Map<String, Object>> selectMyEtfSummary(
            @Param("userId") String userId
    );

    /* =========================
       ETF 요약 (페이징)
       ========================= */
    List<Map<String, Object>> selectMyEtfSummaryPaging(
            @Param("userId") String userId,
            @Param("page") PageRequestDTO page
    );

    /* =========================
       ETF 개수 (페이징용)
       ========================= */
    int countMyEtf(
            @Param("userId") String userId
    );

    /* =========================
       ETF 종목 목록
       ========================= */
    List<MyEtfItemDTO> selectEtfItemList(
            @Param("userId") String userId,
            @Param("etfName") String etfName,
            @Param("deletedYn") String deletedYn
    );

    /* =========================
       ETF 설명 조회
       ========================= */
    String selectEtfDescription(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );

    /* =========================
       단건 조회
       ========================= */
    MyEtfItemDTO selectByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") String userId
    );

    MyEtfItemDTO selectByUserIdEtfNameCode(
            @Param("userId") String userId,
            @Param("etfName") String etfName,
            @Param("code") String code
    );

    /* =========================
       등록
       ========================= */
    int insertEtfItem(
            @Param("dto") MyEtfItemDTO dto
    );

    /* =========================
       ETF 전체 soft delete
       ========================= */
    int deleteByUserIdAndEtfName(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );

    /* =========================
       ETF 설명 수정
       ========================= */
    int updateEtfDescription(
            @Param("userId") String userId,
            @Param("etfName") String etfName,
            @Param("etfDescription") String etfDescription
    );

    /* =========================
       ETF 이름 목록
       ========================= */
    List<String> selectDistinctEtfName(
            @Param("userId") String userId
    );

    /* =========================
       soft delete 복구 (단건)
       ========================= */
    int restoreById(
            @Param("id") Long id,
            @Param("userId") String userId
    );

    /* =========================
       soft delete (단건)
       ========================= */
    int softDeleteById(
            @Param("id") Long id,
            @Param("userId") String userId
    );

    int countByUserIdAndEtfName(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );
}
