package com.stock.webpage.service;

import com.stock.webpage.dto.*;

import java.util.List;

public interface MyEtfService {

    // ETF 목록 요약
    public PageResponseDTO<MyEtfSummaryDTO> getMyEtfList(
            String userId,
            PageRequestDTO pageRequestDTO
    );


    // ETF 생성
    void createEtf(String userId, MyEtfCreateRequestDTO request);

    // ETF 상세 종목 (View DTO로 변경)
    List<MyEtfItemViewDTO> getEtfItemList(String userId, String etfName);

    // ETF 설명 조회
    String getEtfDescription(String userId, String etfName);

    // ETF 편집
    void editEtf(String userId, MyEtfEditRequestDTO request);

    // ETF 종목 복구
    void restoreEtfItems(String userId, MyEtfRestoreRequestDTO request);

    // ETF 상세 요약
    MyEtfDetailSummaryDTO getEtfDetailSummary(String userId, String etfName);

    // ETF 삭제
    void deleteEtf(String userId, String etfName);

    // ETF 종목 복구 이력 조회 (추가)
    List<MyEtfItemHistoryDTO> getEtfItemRestoreHistory(
            String userId,
            String etfName
    );
}
