package com.stock.webpage.mapper;

import com.stock.webpage.dto.MyStockDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyStockMapper {

    // =========================
    // 조회
    // =========================

    // 기존: 전체 목록 (유지 – 다른 곳에서 쓰고 있음)
    List<MyStockDTO> selectMyStockList(String userId);

    List<MyStockDTO> selectMyStockListPagingKR(@Param("userId") String userId, @Param("page") PageRequestDTO page);
    int countMyStockKR(@Param("userId") String userId);

    List<MyStockDTO> selectMyStockListPagingUS(@Param("userId") String userId, @Param("page") PageRequestDTO page);
    int countMyStockUS(@Param("userId") String userId);


    // 삭제된 목록
    List<MyStockDTO> selectDeletedList(String userId);

    // 단건 조회 (ID)
    MyStockDTO selectById(Long id);

    // 단건 조회 (userId + code)
    MyStockDTO selectMyStock(String userId, String code);

    // =========================
    // 등록 / 수정
    // =========================

    int existsMyStock(String userId, String code);

    int insertMyStock(MyStockDTO dto);

    int softDeleteMyStock(Long id, String userId);

    int restoreMyStock(Long id, String userId);

    List<MyStockDTO> selectDeletedListPaging(
            @Param("userId") String userId,
            @Param("page") PageRequestDTO page
    );

    int countDeletedList(@Param("userId") String userId);
}

