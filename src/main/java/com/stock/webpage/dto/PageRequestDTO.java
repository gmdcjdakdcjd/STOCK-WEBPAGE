package com.stock.webpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;
    private String keyword;
    private String regDate;

    private String link;

    private String execDate;   // 배치 실행 날짜 (YYYYMMDD)


    // =========================
    // MyBatis용 페이징 계산
    // =========================
    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    // =========================
    // 검색 타입 처리
    // =========================
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return new String[]{type};
    }

    // =========================
    // 링크 생성 (유지)
    // =========================
    public String getLink() {

        if (link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=").append(this.page);
            builder.append("&size=").append(this.size);

            if (type != null && !type.isEmpty()) {
                builder.append("&type=").append(type);
            }

            if (keyword != null) {
                try {
                    builder.append("&keyword=")
                            .append(URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // ignore
                }
            }

            if (regDate != null && !regDate.isEmpty()) {
                builder.append("&regDate=").append(regDate);
            }

            if (execDate != null && !execDate.isEmpty()) {
                builder.append("&execDate=").append(execDate);
            }

            link = builder.toString();
        }

        return link;
    }

    public int getLimitPlusOne() {
        return size + 1;
    }
}
