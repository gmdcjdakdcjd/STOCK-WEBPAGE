package com.stock.webpage.api;

import com.stock.webpage.dto.MemberJoinDTO;
import com.stock.webpage.dto.MemberSecurityDTO;
import com.stock.webpage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final MemberService memberService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    /*
     * =========================
     * 로그인 상태 확인
     * =========================
     */
    @GetMapping("/me")
    public MemberSecurityDTO me(
            @AuthenticationPrincipal MemberSecurityDTO user) {
        return user; // 인증 안 됨 → ApiAuthenticationEntryPoint → 401
    }

    /*
     * =========================
     * 로그아웃
     * =========================
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Map.of("result", "OK");
    }

    /*
     * =========================
     * 회원가입
     * =========================
     */
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
                            "reason", "MID_DUPLICATE"));

        } catch (MemberService.EmailExistException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "result", "FAIL",
                            "reason", "EMAIL_DUPLICATE"));
        }
    }

    /*
     * =========================
     * 게스트 로그인 처리
     * guest 계정이 없을 경우 회원가입 후 로그인을 처리합니다.
     * =========================
     */
    @PostMapping("/guest")
    public ResponseEntity<?> guestLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            // guest 계정이 DB에 없다면 회원가입을 먼저 진행합니다.
            if (memberService.isMidAvailable("guest")) {
                MemberJoinDTO guestJoin = new MemberJoinDTO();
                guestJoin.setMid("guest");
                guestJoin.setMpw("1q2w3e4r!");
                guestJoin.setEmail("guest@example.com");
                guestJoin.setDel(false);
                guestJoin.setSocial(false);
                memberService.join(guestJoin);
            }

            // CustomUserDetailsService를 통해 회원 정보(MemberSecurityDTO)를 조회합니다.
            UserDetails guestUserDetails = userDetailsService.loadUserByUsername("guest");

            // SecurityContext에 인증 정보(Authentication) 객체를 설정합니다.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(guestUserDetails, null,
                    guestUserDetails.getAuthorities());

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(token);

            // 세션 저장소에 인증 컨텍스트를 동기화하여 로그인 상태를 유지시킵니다.
            securityContextRepository.saveContext(context, request, response);

            return ResponseEntity.ok(Map.of("result", "OK"));

        } catch (Exception e) {
            log.error("Guest login failed: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", "GUEST LOGIN ERROR: " + e.getMessage()));
        }
    }

    /*
     * =========================
     * MID 중복 체크
     * =========================
     */
    @GetMapping("/check-mid")
    public boolean checkMid(@RequestParam String mid) {
        return memberService.isMidAvailable(mid);
    }

    /*
     * =========================
     * EMAIL 중복 체크
     * =========================
     */
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.isEmailAvailable(email);
    }

    /*
     * =========================
     * 비밀번호 변경 API (변경 완료 시 세션 즉시 만료 및 로그아웃)
     * =========================
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal MemberSecurityDTO user,
            @RequestBody Map<String, String> param,
            HttpServletRequest request
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        String currentPw = param.get("currentPw");
        String newPw = param.get("newPw");

        try {
            memberService.modifyPassword(user.getMid(), currentPw, newPw);

            // 비밀번호 변경 완료 즉시 세션을 무효화하여 모든 기기의 강제 로그아웃을 유도합니다.
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "reason", "PASSWORD_MISMATCH",
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * =========================
     * 현재 비밀번호 사전 검증 API
     * =========================
     */
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(
            @AuthenticationPrincipal MemberSecurityDTO user,
            @RequestBody Map<String, String> param
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        String password = param.get("password");

        try {
            boolean isMatch = memberService.verifyPassword(user.getMid(), password);
            if (isMatch) {
                return ResponseEntity.ok(Map.of("result", "OK"));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "result", "FAIL",
                        "reason", "PASSWORD_MISMATCH",
                        "message", "현재 비밀번호가 올바르지 않습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * =========================
     * 멤버십 가입 API (PREMIUM 등급 승격)
     * =========================
     */
    @PostMapping("/membership")
    public ResponseEntity<?> joinMembership(
            @AuthenticationPrincipal MemberSecurityDTO user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        try {
            memberService.joinMembership(user.getMid());

            // 로그인 상태 확인 API에서 최신 등급을 반환할 수 있도록 세션 유저 객체 정보도 함께 갱신해줍니다.
            user.setGrade(com.stock.webpage.enums.MemberGrade.PREMIUM.name());

            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * =========================
     * 회원 탈퇴 API (del=1 상태 변경 및 로그아웃)
     * =========================
     */
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @AuthenticationPrincipal MemberSecurityDTO user,
            @RequestBody Map<String, String> param,
            HttpServletRequest request
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        String password = param.get("password");

        try {
            memberService.withdraw(user.getMid(), password);

            // 회원 탈퇴 성공 시 스프링 시큐리티 컨텍스트 및 세션을 파괴하여 완전히 로그아웃 처리합니다.
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "reason", "PASSWORD_MISMATCH",
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * =========================
     * 멤버십 해지 API (BASIC 등급 강등)
     * =========================
     */
    @PostMapping("/membership/cancel")
    public ResponseEntity<?> cancelMembership(
            @AuthenticationPrincipal MemberSecurityDTO user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        try {
            memberService.cancelMembership(user.getMid());

            // 세션 유저의 등급 정보도 BASIC으로 즉시 갱신
            user.setGrade(com.stock.webpage.enums.MemberGrade.BASIC.name());

            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /*
     * =========================
     * 이메일 주소 변경 API
     * =========================
     */
    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(
            @AuthenticationPrincipal MemberSecurityDTO user,
            @RequestBody Map<String, String> param
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("result", "FAIL", "reason", "UNAUTHORIZED"));
        }

        String email = param.get("email");

        try {
            memberService.modifyEmail(user.getMid(), email);

            // 로그인 정보 상태 확인 시 반영되도록 세션 내의 정보도 즉시 갱신합니다.
            user.setEmail(email);

            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "reason", "DUPLICATE_EMAIL",
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()));
        }
    }
}
