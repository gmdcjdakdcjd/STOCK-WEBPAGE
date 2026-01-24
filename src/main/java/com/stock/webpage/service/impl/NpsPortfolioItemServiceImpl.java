package com.stock.webpage.service.impl;

import com.stock.webpage.dto.NpsPortfolioItemDTO;
import com.stock.webpage.mapper.NpsPortfolioItemMapper;
import com.stock.webpage.service.NpsPortfolioItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NpsPortfolioItemServiceImpl
        implements NpsPortfolioItemService {

    private final NpsPortfolioItemMapper mapper;

    @Override
    public List<NpsPortfolioItemDTO> getItemList(
            String institutionCode,
            String asset,
            String market,
            String q
    ) {

        List<NpsPortfolioItemDTO> list =
                mapper.selectItemList(
                        institutionCode,
                        asset,
                        market,
                        q
                );

        // 여기서 화면용 값 세팅
        list.forEach(dto -> {
            if (dto.getEvalAmount100m() != null) {
                dto.setEvalAmountDisplay(
                        formatEok(dto.getEvalAmount100m())
                );
            }
        });

        return list;
    }

    @Override
    public List<String> autocompleteNames(
            String institutionCode,
            String asset,
            String market,
            String q
    ) {
        return mapper.selectAutocompleteNames(
                institutionCode,
                asset,
                market,
                q
        );
    }

    // 억 단위 포맷 (소수점 제거 + 콤마)
    private String formatEok(BigDecimal value) {
        return String.format("%,d", value.longValue());
    }
}
