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

        // 가입 시 입력된 등급이 없으면 기본 등급(BASIC)으로 설정합니다.
        String grade = dto.getGrade();
        if (grade == null || grade.trim().isEmpty()) {
            grade = com.stock.webpage.enums.MemberGrade.BASIC.name();
        }

        MemberDTO member = MemberDTO.builder()
                .mid(dto.getMid())
                .mpw(passwordEncoder.encode(dto.getMpw()))
                .email(dto.getEmail())
                .del(false)
                .social(dto.isSocial())
                .grade(grade)
                .build();

        int cnt = memberMapper.insertMember(member);
        if (cnt != 1) {
            throw new RuntimeException("회원 생성 실패");
        }

        // 기본 권한 부여
        memberMapper.insertRole(dto.getMid(), "USER");
        log.info("신규 회원가입 완료: {}", dto.getMid());
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

    /* =========================
       관리자 전용 기능 (회원 관리)
       ========================= */

    /**
     * 전체 회원 목록을 조회합니다.
     */
    @Override
    public List<MemberDTO> getAllMembers() {
        return memberMapper.selectAllMembers();
    }

    /**
     * 회원의 이용 등급을 변경합니다.
     */
    @Override
    public void modifyMemberGrade(String mid, String grade) {
        // 등급 정보 검증 (BASIC, PREMIUM)
        try {
            com.stock.webpage.enums.MemberGrade.valueOf(grade.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 등급 정보입니다. (BASIC, PREMIUM만 가능): " + grade);
        }

        int count = memberMapper.updateMemberGrade(mid, grade.toUpperCase());
        if (count != 1) {
            throw new RuntimeException("회원 등급 수정 처리에 실패했습니다. (아이디: " + mid + ")");
        }
    }

    /**
     * 회원의 비밀번호를 안전하게 수정하는 비즈니스 로직입니다.
     * 현재 사용 중인 비밀번호 검증 과정을 거친 후 암호화하여 저장합니다.
     */
    @Override
    public void modifyPassword(String mid, String currentPw, String newPw) {
        MemberDTO member = memberMapper.selectMemberByMid(mid);
        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        // 현재 비밀번호 일치 검증
        if (!passwordEncoder.matches(currentPw, member.getMpw())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호와 현재 비밀번호가 동일한지 검증
        if (currentPw.equals(newPw)) {
            throw new IllegalArgumentException("변경할 새 비밀번호가 이전 비밀번호와 동일합니다. 다른 비밀번호를 입력해 주십시오.");
        }

        // 새 비밀번호 암호화 후 변경 처리
        String encodedNewPw = passwordEncoder.encode(newPw);
        int count = memberMapper.updatePassword(mid, encodedNewPw);
        if (count != 1) {
            throw new RuntimeException("비밀번호 변경 수정 처리에 실패했습니다.");
        }
    }

    /**
     * 회원을 유료 프리미엄 멤버십 등급으로 가입 및 등급 전환 처리합니다.
     */
    @Override
    public void joinMembership(String mid) {
        int count = memberMapper.updateMemberGrade(mid, com.stock.webpage.enums.MemberGrade.PREMIUM.name());
        if (count != 1) {
            throw new RuntimeException("멤버십 가입 처리에 실패했습니다.");
        }
    }

    /**
     * 회원의 회원 탈퇴 비즈니스 로직입니다.
     * 본인 확인을 위해 입력받은 패스워드를 비교하고 검증한 후 Soft Delete(del=1)를 마칩니다.
     */
    @Override
    public void withdraw(String mid, String password) {
        MemberDTO member = memberMapper.selectMemberByMid(mid);
        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        // 탈퇴 전 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getMpw())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 비활성화(Soft Delete) 쿼리 수행
        int count = memberMapper.updateDelStatus(mid);
        if (count != 1) {
            throw new RuntimeException("회원 탈퇴 데이터 수정 처리에 실패했습니다.");
        }
    }

    /**
     * 프리미엄 멤버십 권한을 해지하고 BASIC 등급으로 다시 돌려놓는 비즈니스 로직입니다.
     */
    @Override
    public void cancelMembership(String mid) {
        int count = memberMapper.updateMemberGrade(mid, com.stock.webpage.enums.MemberGrade.BASIC.name());
        if (count != 1) {
            throw new RuntimeException("멤버십 해지 처리에 실패했습니다.");
        }
    }

    /**
     * 회원의 원본 비밀번호가 데이터베이스에 저장된 암호와 일치하는지 비교합니다.
     */
    @Override
    public boolean verifyPassword(String mid, String rawPassword) {
        MemberDTO member = memberMapper.selectMemberByMid(mid);
        if (member == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, member.getMpw());
    }

    /**
     * 회원의 이메일 주소를 변경합니다.
     * 이메일 중복 체크를 통해 이미 가입된 다른 사용자의 이메일과 중복되지 않도록 방지합니다.
     */
    @Override
    public void modifyEmail(String mid, String newEmail) {
        MemberDTO member = memberMapper.selectMemberByMid(mid);
        if (member == null) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        // 기존 이메일과 같은 경우 업데이트 생략
        if (newEmail.equals(member.getEmail())) {
            return;
        }

        // 이미 사용 중인 이메일인지 중복 확인
        boolean isAvailable = isEmailAvailable(newEmail);
        if (!isAvailable) {
            throw new IllegalArgumentException("이미 가입에 등록되어 사용 중인 이메일 주소입니다.");
        }

        int count = memberMapper.updateEmail(mid, newEmail);
        if (count != 1) {
            throw new RuntimeException("이메일 정보 변경 데이터 수정 처리에 실패했습니다.");
        }
    }
}
