package com.stock.webpage.service.impl;

import com.stock.webpage.dto.StrategyCodeDTO;
import com.stock.webpage.dto.StrategyDetailDTO;
import com.stock.webpage.dto.StrategyResultDTO;
import com.stock.webpage.enums.StrategyCode;
import com.stock.webpage.mapper.StrategyDetailMapper;
import com.stock.webpage.mapper.StrategyResultMapper;
import com.stock.webpage.service.StrategyDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyDetailServiceImpl implements StrategyDetailService {

    private final StrategyResultMapper strategyResultMapper;
    private final StrategyDetailMapper strategyDetailMapper;

    @Override
    public List<StrategyDetailDTO> getLatestOrToday(String strategyName, String today) {

        LocalDate targetDate = LocalDate.parse(today);

        // US 전략은 하루 전 기준
        if (strategyName.endsWith("_US")) {
            targetDate = targetDate.minusDays(1);
        }

        boolean exists =
                strategyResultMapper.existsByStrategyNameAndSignalDate(strategyName, targetDate);

        if (!exists) {
            StrategyResultDTO latest =
                    strategyResultMapper.selectLatestByStrategyName(strategyName);

            if (latest == null) {
                return List.of();
            }

            targetDate = latest.getSignalDate();
        }

        List<StrategyDetailDTO> list =
                strategyDetailMapper.selectByActionAndSignalDateOrderBySpecialValueAsc(
                        strategyName,
                        targetDate
                );

        return formatPrice(strategyName, list);
    }

    @Override
    public List<StrategyDetailDTO> getDetail(String strategy, LocalDate date) {
        return strategyDetailMapper
                .selectByActionAndSignalDateOrderBySpecialValueAsc(strategy, date);
    }

    @Override
    public List<StrategyDetailDTO> searchDetail(String keyword) {

        List<StrategyDetailDTO> list =
                strategyDetailMapper.selectByKeyword(keyword);

        for (StrategyDetailDTO dto : list) {
            StrategyCode sc = StrategyCode.findByCode(dto.getAction());

            if (sc != null) {
                dto.setStrategy(
                        new StrategyCodeDTO(sc.getCode(), sc.getLabel())
                );
            }
        }

        return list;
    }


    // -----------------------------
    // 가격 포맷 처리
    // -----------------------------
    private List<StrategyDetailDTO> formatPrice(String strategyName,
                                                List<StrategyDetailDTO> list) {

        boolean isKR = strategyName.endsWith("_KR");
        boolean isUS = strategyName.endsWith("_US");

        for (StrategyDetailDTO s : list) {

            if (isKR) {
                s.setPrice(floor0(s.getPrice()));
                s.setPrevClose(floor0(s.getPrevClose()));
                s.setDiff(floor0(s.getDiff()));
            }
            else if (isUS) {
                s.setPrice(floor2(s.getPrice()));
                s.setPrevClose(floor2(s.getPrevClose()));
                s.setDiff(floor2(s.getDiff()));
            }
        }

        return list;
    }

    private Double floor0(Double v) {
        if (v == null) return null;
        return Math.floor(v);
    }

    private Double floor2(Double v) {
        if (v == null) return null;
        return Math.floor(v * 100) / 100.0;
    }
}
