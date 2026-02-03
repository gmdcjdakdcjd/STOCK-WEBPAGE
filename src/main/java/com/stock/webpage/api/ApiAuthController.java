package com.stock.webpage.api;

import com.stock.webpage.dto.MemberJoinDTO;
import com.stock.webpage.dto.MemberSecurityDTO;
import com.stock.webpage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final MemberService memberService;

    /* =========================
       로그인 상태 확인
       ========================= */
    @GetMapping("/me")
    public MemberSecurityDTO me(
            @AuthenticationPrincipal MemberSecurityDTO user
    ) {
        return user; // 인증 안 됨 → ApiAuthenticationEntryPoint → 401
    }

    /* =========================
       로그아웃
       ========================= */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Map.of("result", "OK");
    }

    /* =========================
       회원가입
       ========================= */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberJoinDTO dto) {

        try {
            memberService.join(dto);
            return ResponseEntity.ok(Map.of("result", "OK"));

        } catch (MemberService.MidExistException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "result", "FAIL",
                            "reason", "MID_DUPLICATE"
                    ));

        } catch (MemberService.EmailExistException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "result", "FAIL",
                            "reason", "EMAIL_DUPLICATE"
                    ));
        }
    }


    /* =========================
       MID 중복 체크
       ========================= */
    @GetMapping("/check-mid")
    public boolean checkMid(@RequestParam String mid) {
        return memberService.isMidAvailable(mid);
    }

    /* =========================
       EMAIL 중복 체크
       ========================= */
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.isEmailAvailable(email);
    }
}
