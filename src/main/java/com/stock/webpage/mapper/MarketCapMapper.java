package com.stock.webpage.mapper;

import com.stock.webpage.dto.MarketCapDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MarketCapMapper {

    List<MarketCapDTO> selectMarketCapList();

}
