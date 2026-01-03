package com.stock.webpage.mapper;

import com.stock.webpage.dto.StrategyResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StrategyResultMapper {

    // existsByStrategyNameAndSignalDate
    boolean existsByStrategyNameAndSignalDate(
            @Param("strategyName") String strategyName,
            @Param("signalDate") LocalDate signalDate
    );

    // findTopByStrategyNameOrderBySignalDateDesc
    StrategyResultDTO selectLatestByStrategyName(
            @Param("strategyName") String strategyName
    );

    // 통합 조회 (KR / US 공용)
    List<StrategyResultDTO> selectStrategyResults(
            @Param("strategies") List<String> strategies,
            @Param("strategy") String strategy,
            @Param("signalDate") LocalDate signalDate,
            @Param("suffix") String suffix,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // pagination용 count
    int countStrategyResults(
            @Param("strategies") List<String> strategies,
            @Param("strategy") String strategy,
            @Param("signalDate") LocalDate signalDate,
            @Param("suffix") String suffix
    );
}
