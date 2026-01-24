package com.stock.webpage.service.impl;

import com.stock.webpage.dto.MemberDTO;
import com.stock.webpage.dto.MemberJoinDTO;
import com.stock.webpage.mapper.MemberMapper;
import com.stock.webpage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    /* =========================
       회원가입
       ========================= */
    @Override
    public void join(MemberJoinDTO dto)
            throws MidExistException, EmailExistException {

        // Service 메서드 재사용 (정석)
        if (!isMidAvailable(dto.getMid())) {
            throw new MidExistException();
        }

        if (!isEmailAvailable(dto.getEmail())) {
            throw new EmailExistException();
        }

        MemberDTO member = MemberDTO.builder()
                .mid(dto.getMid())
                .mpw(passwordEncoder.encode(dto.getMpw()))
                .email(dto.getEmail())
                .del(false)
                .social(dto.isSocial())
                .build();

        int cnt = memberMapper.insertMember(member);
        if (cnt != 1) {
            throw new RuntimeException("회원 생성 실패");
        }

        // 기본 권한 부여
        memberMapper.insertRole(dto.getMid(), "USER");

        log.info("회원가입 완료: {}", dto.getMid());
    }

    /* =========================
       중복 체크
       ========================= */
    @Override
    public boolean isMidAvailable(String mid) {
        return memberMapper.selectMemberByMid(mid) == null;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return memberMapper.selectMemberByEmail(email) == null;
    }

    /* =========================
       로그인 / Security 용
       ========================= */

    /**
     * 아이디 또는 이메일로 회원 조회
     */
    @Override
    public MemberDTO getMemberByMidOrEmail(String keyword) {
        return memberMapper.selectMemberByMidOrEmail(keyword);
    }

    /**
     * 권한 조회 (MID 기준)
     */
    @Override
    public List<String> getRolesByMid(String mid) {
        return memberMapper.selectRolesByMid(mid);
    }
}
