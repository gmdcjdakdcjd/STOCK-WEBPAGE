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
}
