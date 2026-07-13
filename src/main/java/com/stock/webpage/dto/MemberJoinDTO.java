package com.stock.webpage.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {

    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

    // 회원가입 시 설정할 회원의 등급 정보입니다.
    private String grade;
}
