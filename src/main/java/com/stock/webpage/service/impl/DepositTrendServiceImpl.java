package com.stock.webpage.service.impl;

import com.stock.webpage.dto.DepositTrendBoxDTO;
import com.stock.webpage.dto.DepositTrendDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.mapper.DepositTrendMapper;
import com.stock.webpage.service.DepositTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositTrendServiceImpl implements DepositTrendService {


    private final DepositTrendMapper depositTrendMapper;

    // 오늘/어제 비교 계산
    @Override
    public DepositTrendBoxDTO getDepositTrendBox() {

        List<DepositTrendDTO> list =
                depositTrendMapper.selectLatestTwoDepositTrend();

        if (list == null || list.size() < 2) {
            return null;
        }



        DepositTrendDTO today = list.get(0);
        DepositTrendDTO yesterday = list.get(1);

        long custDiff = diff(today.getCustDeposit(), yesterday.getCustDeposit());
        long creditDiff = diff(today.getCreditBalance(), yesterday.getCreditBalance());
        long stockDiff = diff(today.getStockFund(), yesterday.getStockFund());
        long mixedDiff = diff(today.getMixedFund(), yesterday.getMixedFund());
        long bondDiff = diff(today.getBondFund(), yesterday.getBondFund());

        long totalFundToday =
                nvl(today.getStockFund()) +
                        nvl(today.getMixedFund()) +
                        nvl(today.getBondFund());

        long totalFundYesterday =
                nvl(yesterday.getStockFund()) +
                        nvl(yesterday.getMixedFund()) +
                        nvl(yesterday.getBondFund());

        long totalFundDiff = diff(totalFundToday, totalFundYesterday);

        return DepositTrendBoxDTO.builder()
                .baseDate(today.getBaseDate())

                .custDeposit(today.getCustDeposit())
                .custDepositDiff(custDiff)
                .custDepositUp(custDiff > 0)

                .creditBalance(today.getCreditBalance())
                .creditBalanceDiff(creditDiff)
                .creditBalanceUp(creditDiff > 0)

                .stockFund(today.getStockFund())
                .stockFundDiff(stockDiff)
                .stockFundUp(stockDiff > 0)

                .mixedFund(today.getMixedFund())
                .mixedFundDiff(mixedDiff)
                .mixedFundUp(mixedDiff > 0)

                .bondFund(today.getBondFund())
                .bondFundDiff(bondDiff)
                .bondFundUp(bondDiff > 0)

                .totalFund(totalFundToday)
                .totalFundDiff(totalFundDiff)
                .totalFundUp(totalFundDiff > 0)

                .build();
    }

    // 페이징 리스트
    @Override
    public PageResponseDTO<DepositTrendDTO> getDepositTrendList(PageRequestDTO pageRequestDTO) {

        int total = depositTrendMapper.selectDepositTrendCount();

        List<DepositTrendDTO> list =
                depositTrendMapper.selectDepositTrendList(pageRequestDTO);

        // 🔥 방향 계산 (오늘 vs 다음 row 비교)
        for (int i = 0; i < list.size() - 1; i++) {

            DepositTrendDTO current = list.get(i);
            DepositTrendDTO next = list.get(i + 1);

            current.setCustDepositUp(
                    diff(current.getCustDeposit(), next.getCustDeposit()) > 0
            );

            current.setCreditBalanceUp(
                    diff(current.getCreditBalance(), next.getCreditBalance()) > 0
            );

            current.setStockFundUp(
                    diff(current.getStockFund(), next.getStockFund()) > 0
            );

            current.setMixedFundUp(
                    diff(current.getMixedFund(), next.getMixedFund()) > 0
            );

            current.setBondFundUp(
                    diff(current.getBondFund(), next.getBondFund()) > 0
            );
        }

        // 마지막 비교용 데이터 제거
        if (list.size() > pageRequestDTO.getSize()) {
            list.remove(list.size() - 1);
        }

        return PageResponseDTO.<DepositTrendDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    private long diff(Long today, Long yesterday) {
        long t = (today == null) ? 0L : today;
        long y = (yesterday == null) ? 0L : yesterday;
        return t - y;
    }

    private long nvl(Long value) {
        return value == null ? 0L : value;
    }
}