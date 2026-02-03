package com.stock.webpage.service;

import com.stock.webpage.dto.MyStockDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;

import java.util.List;

public interface MyStockService {

    // =========================
    // 기존 기능 (유지)
    // =========================
    void addBatch(String userId, List<MyStockDTO> list);

    List<MyStockDTO> getMyStockList(String userId);

    void delete(Long id, String userId);

    List<MyStockDTO> getDeletedList(String userId);

    void restore(Long id, String userId);

    // =========================
    // 페이징 전용 (추가)
    // =========================
    PageResponseDTO<MyStockDTO> getMyStockListKR(String userId, PageRequestDTO page);
    PageResponseDTO<MyStockDTO> getMyStockListUS(String userId, PageRequestDTO page);

    PageResponseDTO<MyStockDTO> getDeletedListPaging(
            String userId, PageRequestDTO dto
    );
}
