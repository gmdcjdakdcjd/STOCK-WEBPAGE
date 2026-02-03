package com.stock.webpage.api;

import com.stock.webpage.service.AutocompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class AutocompleteApiController {

    private final AutocompleteService autocompleteService;

    // 종목명 자동완성 (React용)
    @GetMapping("/autocomplete/stock")
    public List<Map<String, String>> autocompleteApi(
            @RequestParam("q") String keyword
    ) {
        return autocompleteService.search(keyword);
    }

    // 종목코드 자동완성 (React용)
    @GetMapping("/autocomplete/code")
    public List<Map<String, String>> autocompleteApiCode(
            @RequestParam("q") String keyword
    ) {
        return autocompleteService.search(keyword);
    }
}
