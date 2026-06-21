package com.stock.webpage.service;

import com.stock.webpage.dto.StrategyDetailDTO;

import java.time.LocalDate;
import java.util.List;

public interface StrategyDetailService {

    List<StrategyDetailDTO> getLatestOrToday(String strategyName, String today);

    List<StrategyDetailDTO> getDetail(String strategy, LocalDate date);

    List<StrategyDetailDTO> searchDetail(String keyword);

    /**
     * 특정 키워드(종목코드/종목명) 기준 전략 포착 이력(시그널)을 페이징 처리하여 조회합니다.
     *
     * @param keyword 종목코드 또는 종목명
     * @param page 페이지 번호 (1부터 시작)
     * @param size 한 페이지당 조회할 개수
     * @return 가공된 시그널 정보 리스트
     */
    List<StrategyDetailDTO> searchDetailPaged(String keyword, int page, int size);
}
