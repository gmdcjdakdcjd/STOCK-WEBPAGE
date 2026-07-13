package com.stock.webpage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

    // 회원의 서비스 이용 등급을 나타내는 필드입니다. (예: BASIC, PREMIUM)
    private String grade;
}
