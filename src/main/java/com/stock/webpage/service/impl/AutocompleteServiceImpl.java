package com.stock.webpage.service.impl;

import com.stock.webpage.dto.CompanyInfoKrDTO;
import com.stock.webpage.dto.CompanyInfoUsDTO;
import com.stock.webpage.mapper.CompanyInfoKrMapper;
import com.stock.webpage.mapper.CompanyInfoUsMapper;
import com.stock.webpage.service.AutocompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;


@Service
@RequiredArgsConstructor
public class AutocompleteServiceImpl implements AutocompleteService {

    private static final int LIMIT = 10;

    private final CompanyInfoKrMapper companyInfoKrMapper;
    private final CompanyInfoUsMapper companyInfoUsMapper;

    private String formatPrice(Number price, String market) {
        if (price == null) {
            return "US".equals(market) ? "$0.00" : "0원";
        }

        BigDecimal bd = new BigDecimal(price.toString());

        if ("US".equals(market)) {
            // 달러, 소수 2자리
            NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
            return usFormat.format(
                    bd.setScale(2, RoundingMode.HALF_UP)
            );
        }

        // KR: 콤마 + 원
        NumberFormat krFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return krFormat.format(
                bd.setScale(0, RoundingMode.DOWN)
        ) + "원";
    }



    @Override
    public List<Map<String, String>> search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        List<Map<String, String>> result = new ArrayList<>();

        // 🇰🇷 KR
        List<CompanyInfoKrDTO> krList =
                companyInfoKrMapper.selectTopByNameOrCodeContaining(keyword, LIMIT);

        for (CompanyInfoKrDTO c : krList) {
            result.add(Map.of(
                    "code", c.getCode(),
                    "name", c.getName(),
                    "market", "KR",
                    "price", formatPrice(c.getPrice(), "KR")
            ));
        }


        // 🇺🇸 US
        List<CompanyInfoUsDTO> usList =
                companyInfoUsMapper.selectTopByNameOrCodeContaining(keyword, LIMIT);

        for (CompanyInfoUsDTO c : usList) {
            result.add(Map.of(
                    "code", c.getCode(),
                    "name", c.getName(),
                    "market", "US",
                    "price", formatPrice(c.getPrice(), "US")
            ));
        }


        return result;
    }


}
