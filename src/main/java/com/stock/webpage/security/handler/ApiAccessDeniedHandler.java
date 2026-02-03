package com.stock.webpage.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        log.info("-------- API ACCESS DENIED --------");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        String accept = request.getHeader("Accept");
        boolean jsonRequest =
                accept != null && accept.contains("application/json");

        if (jsonRequest) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "status": 403,
                  "error": "FORBIDDEN",
                  "message": "ACCESS DENIED"
                }
            """);
        } else {
            // 혹시 API 아닌 요청이 섞였을 때 안전장치
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }
}
