package com.stock.webpage.service.impl;

import com.stock.webpage.dto.ScreenerConditionDTO;
import com.stock.webpage.mapper.ScreenerConditionMapper;
import com.stock.webpage.service.ScreenerConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScreenerConditionServiceImpl implements ScreenerConditionService {

    private final ScreenerConditionMapper screenerConditionMapper;

    @Override
    @Transactional
    public void saveCondition(String userid, ScreenerConditionDTO dto) {
        dto.setUserid(userid);

        if (dto.getId() == null) {
            // ==========================================
            // [INSERT] 새로운 조건식 신규 저장 프로세스
            // ==========================================
            log.info("사용자 조건식 신규 저장(부모 마스터)을 요청합니다. userid: {}, 조건식명: {}", userid, dto.getName());
            
            // 1. 부모 마스터 레코드 저장 (user_condition)
            screenerConditionMapper.insertMaster(dto);
            Long conditionId = dto.getId();

            // 2. 자식 테이블: 세부 지표 조건 벌크 인서트 (user_condition_filter)
            if (dto.getFilters() != null && !dto.getFilters().isEmpty()) {
                screenerConditionMapper.insertFilters(conditionId, dto.getFilters());
            }

            // 3. 자식 테이블: 선택된 ETF 필터 벌크 인서트 (user_condition_etf)
            if (dto.getSelectedEtfs() != null && !dto.getSelectedEtfs().isEmpty()) {
                screenerConditionMapper.insertEtfs(conditionId, dto.getSelectedEtfs());
            }
        } else {
            // ==========================================
            // [UPDATE] 기존 조건식 정보 수정 프로세스 (Delete-Insert 패턴)
            // ==========================================
            Long conditionId = dto.getId();
            log.info("사용자 조건식 수정 요청을 처리합니다. ID: {}, userid: {}, 조건식명: {}", conditionId, userid, dto.getName());

            // 1. 부모 레코드 권한 검증 (실제 존재하고 본인 소유인지 대조)
            ScreenerConditionDTO target = screenerConditionMapper.selectMasterById(conditionId);
            if (target == null) {
                throw new IllegalArgumentException("수정하려는 조건식 식별번호가 존재하지 않습니다.");
            }
            if (!target.getUserid().equals(userid)) {
                throw new SecurityException("본인이 저장한 조건식만 수정이 가능합니다.");
            }

            // 2. 부모 마스터 테이블 레코드 수정 (이름, 시장 정보 등)
            screenerConditionMapper.updateMaster(dto);

            // 3. 자식 테이블 1: 기존 스크리닝 지표들을 지우고 벌크 재생성
            screenerConditionMapper.deleteFiltersByConditionId(conditionId);
            if (dto.getFilters() != null && !dto.getFilters().isEmpty()) {
                screenerConditionMapper.insertFilters(conditionId, dto.getFilters());
            }

            // 4. 자식 테이블 2: 기존 ETF 칩 매핑을 지우고 벌크 재생성
            screenerConditionMapper.deleteEtfsByConditionId(conditionId);
            if (dto.getSelectedEtfs() != null && !dto.getSelectedEtfs().isEmpty()) {
                screenerConditionMapper.insertEtfs(conditionId, dto.getSelectedEtfs());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenerConditionDTO> getConditionList(String userid) {
        log.info("사용자의 저장된 조건식 목록을 조회합니다. userid: {}", userid);
        
        // 1. 부모 마스터 조건식 리스트를 조회합니다.
        List<ScreenerConditionDTO> masterList = screenerConditionMapper.selectMasterListByUserId(userid);

        // 2. 각 조건식 마스터 레코드별로 자식 테이블의 조건 세부 정보들을 조회하여 조립합니다.
        for (ScreenerConditionDTO dto : masterList) {
            Long conditionId = dto.getId();

            // 2-1. 지표 조건 리스트 로드
            List<String> filters = screenerConditionMapper.selectFiltersByConditionId(conditionId);
            dto.setFilters(filters != null ? filters : new ArrayList<>());

            // 2-2. ETF 필터 리스트 로드
            List<Map<String, String>> etfs = screenerConditionMapper.selectEtfsByConditionId(conditionId);
            dto.setSelectedEtfs(etfs != null ? etfs : new ArrayList<>());
        }

        return masterList;
    }

    @Override
    @Transactional
    public void deleteCondition(Long id, String userid) {
        log.info("조건식 삭제를 진행합니다. 삭제 대상 ID: {}, 요청 userid: {}", id, userid);
        
        // 삭제하려는 조건식이 존재하는지 본인 식별키 검증용 상세 조회
        ScreenerConditionDTO target = screenerConditionMapper.selectMasterById(id);
        if (target == null) {
            throw new IllegalArgumentException("존재하지 않는 조건식 식별번호입니다.");
        }
        
        if (!target.getUserid().equals(userid)) {
            throw new SecurityException("본인이 저장한 조건식만 삭제가 가능합니다.");
        }

        // 부모 레코드 삭제 실행
        // 테이블 외래키 설정 시 ON DELETE CASCADE 제약 조건이 설정되어 있으므로, 
        // 부모 조건식이 지워지면 자식(지표, ETF) 테이블의 관련 데이터들도 자동으로 연쇄 삭제됩니다.
        screenerConditionMapper.deleteMaster(id, userid);
        log.info("조건식 마스터 및 매핑 데이터가 성공적으로 영구 삭제되었습니다. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenerConditionDTO> getDeletedConditionList(String userid) {
        log.info("사용자의 삭제된 조건식 목록 조회를 시작합니다. userid: {}", userid);
        
        // 1. 삭제된 조건식 마스터 목록을 조회합니다.
        List<ScreenerConditionDTO> masterList = screenerConditionMapper.selectDeletedMasterListByUserId(userid);

        // 2. 각 삭제된 마스터 조건식에 대해 상세 필터 지표와 ETF 매핑 정보를 조회하여 조립합니다.
        for (ScreenerConditionDTO dto : masterList) {
            Long conditionId = dto.getId();

            // 2-1. 지표 조건 리스트 로드
            List<String> filters = screenerConditionMapper.selectFiltersByConditionId(conditionId);
            dto.setFilters(filters != null ? filters : new ArrayList<>());

            // 2-2. ETF 필터 리스트 로드
            List<Map<String, String>> etfs = screenerConditionMapper.selectEtfsByConditionId(conditionId);
            dto.setSelectedEtfs(etfs != null ? etfs : new ArrayList<>());
        }

        return masterList;
    }

    @Override
    @Transactional
    public void restoreCondition(Long id, String userid) {
        log.info("조건식 복구 요청을 처리합니다. 대상 ID: {}, 요청자 userid: {}", id, userid);
        
        // 복구 업데이트를 실행하고, 영향받은 행의 수가 0이면 해당 조건식이 존재하지 않거나 소유권이 없는 경우입니다.
        int affectedRows = screenerConditionMapper.restoreMaster(id, userid);
        if (affectedRows == 0) {
            throw new IllegalArgumentException("존재하지 않거나 본인이 저장한 조건식이 아니므로 복구할 수 없습니다.");
        }
        
        log.info("조건식 복구 처리가 완료되었습니다. ID: {}", id);
    }
}
