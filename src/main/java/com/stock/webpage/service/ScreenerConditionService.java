package com.stock.webpage.service;

import com.stock.webpage.dto.ScreenerConditionDTO;

import java.util.List;

public interface ScreenerConditionService {

    /**
     * 사용자가 조합한 새로운 조건식을 데이터베이스에 저장합니다.
     * @param userid 로그인한 회원 식별자
     * @param dto 프론트엔드로부터 전달된 조건식 데이터 DTO
     */
    void saveCondition(String userid, ScreenerConditionDTO dto);

    /**
     * 특정 사용자가 저장해 놓은 조건식 리스트를 조회합니다.
     * @param userid 로그인한 회원 식별자
     * @return 구조화된 조건식 DTO 리스트
     */
    List<ScreenerConditionDTO> getConditionList(String userid);

    /**
     * 저장된 조건식을 삭제합니다.
     * @param id 조건식 ID
     * @param userid 로그인한 회원 식별자 (본인 소유 여부 검증용)
     */
    void deleteCondition(Long id, String userid);
}
