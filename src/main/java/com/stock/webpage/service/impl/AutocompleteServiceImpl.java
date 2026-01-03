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

@Service
@RequiredArgsConstructor
public class AutocompleteServiceImpl implements AutocompleteService {

    private static final int LIMIT = 10;

    private final CompanyInfoKrMapper companyInfoKrMapper;
    private final CompanyInfoUsMapper companyInfoUsMapper;

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
                    "market", "KR"
            ));
        }

        // 🇺🇸 US
        List<CompanyInfoUsDTO> usList =
                companyInfoUsMapper.selectTopByNameOrCodeContaining(keyword, LIMIT);

        for (CompanyInfoUsDTO c : usList) {
            result.add(Map.of(
                    "code", c.getCode(),
                    "name", c.getName(),
                    "market", "US"
            ));
        }

        return result;
    }
}
