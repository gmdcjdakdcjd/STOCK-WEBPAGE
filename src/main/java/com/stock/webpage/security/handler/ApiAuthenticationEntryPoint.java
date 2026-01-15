package com.stock.webpage.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Log4j2
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.info("-------- API UNAUTHORIZED --------");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String accept = request.getHeader("Accept");
        boolean jsonRequest =
                accept != null && accept.contains("application/json");

        if (jsonRequest) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "status": 401,
                  "error": "UNAUTHORIZED",
                  "message": "LOGIN REQUIRED"
                }
            """);
        } else {
            // 혹시 API 아닌 요청이 섞였을 때 안전장치
            response.sendRedirect("/member/login");
        }
    }
}
