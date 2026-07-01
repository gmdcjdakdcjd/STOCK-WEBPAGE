package com.stock.webpage.mapper;

import com.stock.webpage.dto.ScreenerConditionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScreenerConditionMapper {

    /**
     * 1-1. 부모 조건식 마스터 레코드를 추가합니다 (user_condition)
     * insert 후 generated key가 dto의 id 필드로 세팅됩니다.
     */
    void insertMaster(ScreenerConditionDTO dto);

    /**
     * 1-2. 자식 테이블: 세부 지표 조건 리스트를 벌크 인서트 합니다 (user_condition_filter)
     */
    void insertFilters(@Param("conditionId") Long conditionId, @Param("filters") List<String> filters);

    /**
     * 1-3. 자식 테이블: 선택된 ETF 필터 목록을 벌크 인서트 합니다 (user_condition_etf)
     */
    void insertEtfs(@Param("conditionId") Long conditionId, @Param("etfs") List<Map<String, String>> etfs);

    /**
     * 2-1. 특정 사용자의 조건식 마스터 목록을 조회합니다 (user_condition)
     */
    List<ScreenerConditionDTO> selectMasterListByUserId(@Param("userid") String userid);

    /**
     * 2-2. 조건식에 매핑된 세부 조건 지표 목록을 조회합니다 (user_condition_filter)
     */
    List<String> selectFiltersByConditionId(@Param("conditionId") Long conditionId);

    /**
     * 2-3. 조건식에 매핑된 ETF 필터 목록을 조회합니다 (user_condition_etf)
     */
    List<Map<String, String>> selectEtfsByConditionId(@Param("conditionId") Long conditionId);

    /**
     * 3. 조건식 마스터 단일 레코드를 상세 조회합니다 (user_condition)
     */
    ScreenerConditionDTO selectMasterById(@Param("id") Long id);

    /**
     * 부모 조건식 마스터의 이름 및 정보를 수정합니다 (user_condition)
     */
    void updateMaster(ScreenerConditionDTO dto);

    /**
     * 특정 조건식에 매핑된 세부 지표 조건들을 일괄 삭제합니다 (user_condition_filter)
     */
    void deleteFiltersByConditionId(@Param("conditionId") Long conditionId);

    /**
     * 특정 조건식에 매핑된 ETF 필터 목록을 일괄 삭제합니다 (user_condition_etf)
     */
    void deleteEtfsByConditionId(@Param("conditionId") Long conditionId);

    /**
     * 4. 조건식을 영구 삭제합니다 (ON DELETE CASCADE로 자식 테이블도 함께 지워집니다)
     */
    void deleteMaster(@Param("id") Long id, @Param("userid") String userid);
}
