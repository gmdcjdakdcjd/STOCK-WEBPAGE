package com.stock.webpage.api;

import com.stock.webpage.dto.ScreenerConditionDTO;
import com.stock.webpage.service.ScreenerConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 나만의 조건식(Screener Condition) 저장, 조회, 삭제를 담당하는 컨트롤러입니다.
 *
 * 기존 /api/screener/conditions/** 경로는 배포 환경의 nginx에서
 * /api/screener/** 전체를 FastAPI로 라우팅하기 때문에 404가 발생합니다.
 * 이를 해결하기 위해 /api/mycondition/** 경로로 분리하였습니다.
 */
@Log4j2
@RestController
@RequestMapping("/api/mycondition")
@RequiredArgsConstructor
public class MyConditionApiController {

    // 조건식 저장/조회/삭제를 처리할 비즈니스 서비스 주입
    private final ScreenerConditionService screenerConditionService;

    /**
     * 사용자가 새로 생성한 조건식을 영구 저장하거나 기존 조건식을 수정합니다.
     * dto.getId()가 null이면 새 저장, null이 아니면 기존 수정 처리입니다.
     */
    @PostMapping("/save")
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
    @GetMapping("/list")
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
     * 본인 소유가 아닌 조건식 삭제 시도 시 403 응답을 반환합니다.
     */
    @PostMapping("/delete/{id}")
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
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.error("조건식 삭제 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("조건식 삭제 오류: " + e.getMessage());
        }
    }

    /**
     * 현재 로그인한 사용자가 삭제해 둔(비활성 상태인) 모든 조건식 목록을 조회합니다.
     */
    @GetMapping("/deleted-list")
    public ResponseEntity<?> getDeletedUserConditions(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("로그인 인증 세션 정보가 없습니다.");
        }

        try {
            List<ScreenerConditionDTO> list = screenerConditionService.getDeletedConditionList(user.getUsername());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("삭제된 조건식 목록 조회 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("삭제된 조건식 조회 오류: " + e.getMessage());
        }
    }

    /**
     * 삭제된 특정 조건식을 복구(활성화) 처리합니다.
     */
    @PostMapping("/restore/{id}")
    public ResponseEntity<?> restoreUserCondition(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(401).body("로그인 인증 세션 정보가 없습니다.");
        }

        try {
            screenerConditionService.restoreCondition(id, user.getUsername());
            return ResponseEntity.ok(Map.of("result", "SUCCESS", "message", "조건식이 성공적으로 복구되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("조건식 복구 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(500).body("조건식 복구 오류: " + e.getMessage());
        }
    }
}
