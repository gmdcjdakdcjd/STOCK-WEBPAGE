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
}
