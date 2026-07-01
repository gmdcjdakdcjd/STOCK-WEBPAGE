package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenerConditionDTO {

    private Long id;              // 조건식 식별 기본키 (user_condition.id)
    private String userid;        // 만든이 식별자 (user_condition.userid)
    private String name;          // 조건식 이름
    private String market;        // 대상 시장 (kr 또는 us)

    // 구조화된 자식 테이블 바인딩용 객체 데이터 필드
    private List<String> filters;
    private List<Map<String, String>> selectedEtfs;

    private String createdAt;     // 생성 일시
}
