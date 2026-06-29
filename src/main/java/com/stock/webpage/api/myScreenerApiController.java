package com.stock.webpage.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/screener")
@RequiredArgsConstructor
public class myScreenerApiController {

    // application.properties에서 설정한 fastapi.url 값을 가져옵니다. 설정되지 않은 경우 기본값으로 http://localhost:8000을 사용합니다.
    @Value("${fastapi.url:http://localhost:8000}")
    private String fastApiUrl;

    // 외부 API 통신을 위한 RestTemplate 인스턴스 생성
    private final RestTemplate restTemplate = new RestTemplate();

    // ==========================================
    // 스크리너 필터 조건 처리
    // ==========================================

    @PostMapping("/run")
    public ResponseEntity<?> runScreener(@RequestBody Map<String, Object> filterRequest) {
        log.info("프론트엔드로부터 스크리너 필터 요청을 받았습니다. 데이터: {}", filterRequest);

        // FastAPI의 스크리너 실행 API 엔드포인트 설정
        String targetUrl = fastApiUrl + "/api/screener/run";
        log.info("FastAPI 분석 서버 호출 시작. URL: {}", targetUrl);

        // API 요청용 HTTP 헤더 설정 (JSON 형식 전송 지정)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디와 헤더를 감싸는 HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(filterRequest, headers);

        try {
            // FastAPI 분석 서버에 POST 요청을 전송하고 응답을 ResponseEntity로 받음
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);
            log.info("FastAPI 분석 서버 응답 성공. 상태 코드: {}", response.getStatusCode());
            
            // FastAPI 분석 서버의 응답을 프론트엔드로 전달
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("FastAPI 분석 서버 호출 중 오류가 발생했습니다.", e);
            // 오류 발생 시 프론트엔드에 500 에러와 함께 상세 메시지 전달
            return ResponseEntity.status(500).body("FastAPI 분석 서버 통신 오류: " + e.getMessage());
        }
    }
}
