package com.stock.webpage.service;

import com.stock.webpage.dto.NpsPortfolioItemDTO;

import java.util.List;

public interface NpsPortfolioItemService {

    List<NpsPortfolioItemDTO> getItemList(
            String institutionCode,
            String asset,
            String market,
            String q
    );

    List<String> autocompleteNames(
            String institutionCode,
            String asset,
            String market,
            String q
    );

}
