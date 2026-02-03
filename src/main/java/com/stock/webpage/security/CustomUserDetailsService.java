package com.stock.webpage.security;

import com.stock.webpage.dto.MemberDTO;
import com.stock.webpage.dto.MemberSecurityDTO;
import com.stock.webpage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        log.info("loadUserByUsername: {}", username);

        /* =========================
           회원 조회 (ID or EMAIL)
           ========================= */
        MemberDTO member =
                memberService.getMemberByMidOrEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        /* =========================
           로그인 정책 체크
           ========================= */
        if (member.isDel()) {
            throw new UsernameNotFoundException("Deleted user");
        }

        if (member.isSocial()) {
            throw new UsernameNotFoundException("Social user");
        }

        /* =========================
           권한 조회 (MID 기준)
           ========================= */
        List<SimpleGrantedAuthority> authorities =
                memberService.getRolesByMid(member.getMid())
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

        /* =========================
           Security DTO 생성
           ========================= */
        MemberSecurityDTO securityDTO =
                new MemberSecurityDTO(
                        member.getMid(),      // username은 항상 MID
                        member.getMpw(),
                        member.getEmail(),
                        member.isDel(),
                        member.isSocial(),
                        authorities
                );

        log.info("memberSecurityDTO: {}", securityDTO);

        return securityDTO;
    }
}
