package com.stock.webpage.mapper;

import com.stock.webpage.dto.InvestorFlowDTO;
import com.stock.webpage.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvestorTrendMapper {

    InvestorFlowDTO selectLatestInvestorTrend();

    List<InvestorFlowDTO> selectInvestorTrendList(PageRequestDTO pageRequestDTO);

    int selectInvestorTrendCount();
}