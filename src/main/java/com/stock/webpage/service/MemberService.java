package com.stock.webpage.service;

import com.stock.webpage.dto.MemberDTO;
import com.stock.webpage.dto.MemberJoinDTO;

import java.util.List;

public interface MemberService {

    /* =========================
       회원가입 관련 예외
       ========================= */
    class MidExistException extends Exception {}
    class EmailExistException extends Exception {}

    /* =========================
       회원가입
       ========================= */
    void join(MemberJoinDTO memberJoinDTO)
            throws MidExistException, EmailExistException;

    /* =========================
       중복 체크
       ========================= */
    boolean isMidAvailable(String mid);
    boolean isEmailAvailable(String email);

    /* =========================
       로그인 / 보안용 조회
       ========================= */

    /**
     * 아이디 또는 이메일로 회원 조회
     * (Spring Security에서 사용)
     */
    MemberDTO getMemberByMidOrEmail(String keyword);

    /**
     * 회원 권한 조회 (MID 기준)
     */
    List<String> getRolesByMid(String mid);

    /* =========================
       관리자 전용 기능 (회원 관리)
       ========================= */

    /**
     * 전체 회원 목록을 조회합니다.
     */
    List<MemberDTO> getAllMembers();

    /**
     * 회원의 이용 등급을 변경합니다.
     */
    void modifyMemberGrade(String mid, String grade);

    /**
     * 회원의 비밀번호를 변경합니다.
     * @param mid       회원 아이디
     * @param currentPw 현재 비밀번호
     * @param newPw     변경할 새로운 비밀번호
     */
    void modifyPassword(String mid, String currentPw, String newPw);

    /**
     * 회원을 PREMIUM 이용 멤버십 등급으로 승격 가입시킵니다.
     * @param mid 회원 아이디
     */
    void joinMembership(String mid);

    /**
     * 회원 탈퇴 처리를 진행합니다.
     * @param mid      회원 아이디
     * @param password 본인 확인용 비밀번호
     */
    void withdraw(String mid, String password);

    /**
     * 회원의 프리미엄 멤버십 가입을 해지하고 BASIC 등급으로 강등 처리합니다.
     * @param mid 회원 아이디
     */
    void cancelMembership(String mid);

    /**
     * 입력받은 비밀번호가 로그인 회원의 실제 비밀번호와 일치하는지 검증합니다.
     * @param mid         회원 아이디
     * @param rawPassword 검증할 원본 비밀번호
     * @return 일치 여부
     */
    boolean verifyPassword(String mid, String rawPassword);

    /**
     * 회원의 이메일 주소를 변경합니다. (중복 검증 로직 포함)
     * @param mid      회원 아이디
     * @param newEmail 변경할 새로운 이메일 주소
     */
    void modifyEmail(String mid, String newEmail);
}
