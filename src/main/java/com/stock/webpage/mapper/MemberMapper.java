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
}
