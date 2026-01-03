package com.stock.webpage.controller;

import com.stock.webpage.service.AutocompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class AutocompleteController {

    private final AutocompleteService autocompleteService;

    @GetMapping("/autocomplete")
    public List<Map<String, String>> autocomplete(
            @RequestParam("q") String keyword) {
        return autocompleteService.search(keyword);
    }

    @GetMapping("/autocompleteCode")
    public List<Map<String, String>> autocompleteCode(
            @RequestParam("q") String keyword) {
        return autocompleteService.search(keyword);
    }
}