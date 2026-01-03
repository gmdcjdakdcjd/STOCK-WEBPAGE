package com.stock.webpage.mapper;

import com.stock.webpage.dto.MyEtfItemHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyEtfItemHistoryMapper {

    // =========================
    // 조회
    // =========================

    List<MyEtfItemHistoryDTO> selectByUserIdEtfName(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );

    List<MyEtfItemHistoryDTO> selectByUserIdEtfNameRestoredYn(
            @Param("userId") String userId,
            @Param("etfName") String etfName,
            @Param("restoredYn") String restoredYn
    );

    // =========================
    // 기존 단순 로직
    // =========================

    int insertHistory(MyEtfItemHistoryDTO dto);

    int markRestored(@Param("list") List<Long> historyIds);

    // =========================
    // ⭐ 핵심 단일 SQL 메서드
    // =========================

    /**
     * my_etf_item → my_etf_item_history 백업
     */
    int backupHistoryFromItem(
            @Param("etfItemId") Long etfItemId,
            @Param("userId") String userId
    );

    /**
     * 히스토리 단건 restored 처리
     */
    int markRestoredById(
            @Param("histId") Long histId,
            @Param("userId") String userId
    );

    Long selectItemIdByHistoryId(
            @Param("histId") Long histId,
            @Param("userId") String userId
    );

    int deleteByUserIdAndEtfName(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );
}
