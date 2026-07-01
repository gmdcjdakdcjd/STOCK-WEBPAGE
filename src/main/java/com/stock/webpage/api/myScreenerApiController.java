package com.stock.webpage.api;

import com.stock.webpage.dto.ScreenerConditionDTO;
import com.stock.webpage.service.ScreenerConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    // 조건식 저장을 처리할 비즈니스 서비스 주입
    private final ScreenerConditionService screenerConditionService;

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

    // ==========================================
    // 나만의 조건식 저장 / 조회 / 삭제 API
    // ==========================================

    /**
     * 사용자가 새로 생성한 조건식을 영구 저장합니다.
     */
    @PostMapping("/conditions/save")
    public ResponseEntity<?> saveUserCondition(
            @AuthenticationPrincipal User user,
            @RequestBody ScreenerConditionDTO dto) {
        
        if (user == null) {
            return ResponseEntity.status(401).body("로그인 인증 세션 정보가 없습니다.");
        }

        try {
            // 스프링 시큐리티의 식별자(user.getUsername())를 조건식의 userid로 지정하여 저장합니다.
            screenerConditionService.saveCondition(user.getUsername(), dto);
            return ResponseEntity.ok(Map.of("result", "SUCCESS", "message", "조건식이 성공적으로 저장되었습니다."));
        } catch (Exception e) {
            log.error("조건식 저장 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("조건식 저장 오류: " + e.getMessage());
        }
    }

    /**
     * 현재 로그인한 사용자가 저장해 둔 모든 조건식 리스트를 조회합니다.
     */
    @GetMapping("/conditions/list")
    public ResponseEntity<?> getUserConditions(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("로그인 인증 세션 정보가 없습니다.");
        }

        try {
            List<ScreenerConditionDTO> list = screenerConditionService.getConditionList(user.getUsername());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("조건식 목록 조회 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("조건식 조회 오류: " + e.getMessage());
        }
    }

    /**
     * 지정한 조건식을 삭제 처리합니다.
     */
    @PostMapping("/conditions/delete/{id}")
    public ResponseEntity<?> deleteUserCondition(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        
        if (user == null) {
            return ResponseEntity.status(401).body("로그인 인증 세션 정보가 없습니다.");
        }

        try {
            screenerConditionService.deleteCondition(id, user.getUsername());
            return ResponseEntity.ok(Map.of("result", "SUCCESS", "message", "조건식이 삭제되었습니다."));
        } catch (SecurityException e) {
            return ResponseEntity.status(430).body(e.getMessage());
        } catch (Exception e) {
            log.error("조건식 삭제 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("조건식 삭제 오류: " + e.getMessage());
        }
    }
}
