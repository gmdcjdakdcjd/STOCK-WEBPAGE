package com.stock.webpage.mapper;

import com.stock.webpage.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    /* =========================
       회원 조회
       ========================= */
    MemberDTO selectMemberByMid(@Param("mid") String mid);

    MemberDTO selectMemberByEmail(@Param("email") String email);

    // ID 또는 EMAIL 로그인용 (추가)
    MemberDTO selectMemberByMidOrEmail(@Param("keyword") String keyword);

    /* =========================
       회원 등록 / 수정
       ========================= */
    int insertMember(MemberDTO member);

    // 탈퇴 회원 재가입 처리 (del=0, regdate=NOW, deldate=NULL)
    int rejoinMember(MemberDTO member);

    // 동일 mid 탈퇴 회원 이력 보존용 아카이빙 처리 (mid 변경)
    int archiveDeletedMember(@Param("mid") String mid);

    // 동일 email 탈퇴 회원 이력 보존용 아카이빙 처리 (email 변경)
    int archiveDeletedMemberByEmail(@Param("email") String email);

    int updatePassword(
            @Param("mid") String mid,
            @Param("mpw") String mpw
    );

    /* =========================
       권한
       ========================= */
    List<String> selectRolesByMid(@Param("mid") String mid);

    int insertRole(
            @Param("mid") String mid,
            @Param("role") String role
    );

    int deleteRolesByMid(@Param("mid") String mid);

    /* =========================
       관리자 전용 기능 (회원 관리)
       ========================= */
    List<MemberDTO> selectAllMembers();

    int updateMemberGrade(
            @Param("mid") String mid,
            @Param("grade") String grade
    );

    int updateDelStatus(@Param("mid") String mid);

    int updateEmail(
            @Param("mid") String mid,
            @Param("email") String email
    );
}
