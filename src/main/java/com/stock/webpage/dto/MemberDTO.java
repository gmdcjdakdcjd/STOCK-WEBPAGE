package com.stock.webpage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    // 회원 고유 번호 (AUTO_INCREMENT PK)
    private Long mno;

    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

    // 회원의 서비스 이용 등급을 나타내는 필드입니다. (예: BASIC, PREMIUM)
    private String grade;

    // 회원의 가입 일자 정보를 나타내는 필드입니다. (예: 2026-07-21 20:30:00)
    private String regDate;

    // 회원의 탈퇴 일자 정보를 나타내는 필드입니다. (예: 2026-07-21 20:33:00)
    private String delDate;
}
