package com.stock.webpage.config;

import com.stock.webpage.security.CustomUserDetailsService;
import com.stock.webpage.security.handler.ApiAccessDeniedHandler;
import com.stock.webpage.security.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Order(1) // 기존 CustomSecurityConfig 보다 우선
public class ApiSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        log.info("------------ API security configure ----------------");

        http
                .securityMatcher("/api/**")

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .maximumSessions(1)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/me",
                                "/api/auth/join",
                                "/api/auth/check-mid",
                                "/api/auth/check-email"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 핵심: formLogin을 완전히 끄지 말고 API용으로 설정
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                    { "result": "OK" }
                """);
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                    { "result": "FAIL", "message": "LOGIN FAILED" }
                """);
                        })
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new ApiAuthenticationEntryPoint())
                        .accessDeniedHandler(new ApiAccessDeniedHandler())
                )

                .userDetailsService(userDetailsService);

        return http.build();
    }

}
