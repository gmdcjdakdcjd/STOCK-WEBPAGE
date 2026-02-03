package com.stock.webpage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
                "status", "OK",
                "message", "board test success",
                "time", LocalDateTime.now().toString()
        );
    }
}