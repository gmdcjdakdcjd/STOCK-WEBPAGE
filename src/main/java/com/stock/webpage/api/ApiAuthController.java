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
}
