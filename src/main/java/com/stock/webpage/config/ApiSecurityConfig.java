package com.stock.webpage.config;

import com.stock.webpage.security.CustomUserDetailsService;
import com.stock.webpage.security.handler.ApiAccessDeniedHandler;
import com.stock.webpage.security.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Order(1)
public class ApiSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        log.info("------------ API security configure ----------------");

        http
                // API 요청만 이 체인이 처리
                .securityMatcher("/api/**")

                // CSRF 비활성 (SPA + fetch 기준)
                .csrf(csrf -> csrf.disable())

                // 세션 기반 인증 유지
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                )

                // API 접근 권한
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능 (공개 조회)
                        .requestMatchers(
                                "/api/auth/**",

                                "/api/indicator",
                                "/api/stock/**",
                                "/api/bond",
                                "/api/issue",
                                "/api/dual-momentum/**",
                                "/api/result/**",
                                "/api/nps/**",
                                "/api/kodex/**",
                                "/api/common/**",
                                "/api/exchange",
                                "/api/physical",
                                "/api/stockIndex",
                                "/api/crypto",
                                "/api/marketCap",
                                "/api/market-trend/**"
                        ).permitAll()

                        // 로그인 필요 (개인화 데이터)
                        .requestMatchers(
                                "/api/mystock/**",
                                "/api/myetf/**",
                                "/api/manage/**"
                        ).authenticated()

                        // 그 외
                        .anyRequest().denyAll()
                )


                // API 로그인 (JSON 응답)
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                        {
                                          "result": "OK"
                                        }
                                    """);
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                        {
                                          "result": "FAIL",
                                          "message": "LOGIN FAILED"
                                        }
                                    """);
                        })
                )

                // 로그아웃 (리다이렉트 없음, SPA에서 처리)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                        {
                                          "result": "OK"
                                        }
                                    """);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )


                // 인증/권한 예외는 JSON으로만 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new ApiAuthenticationEntryPoint())
                        .accessDeniedHandler(new ApiAccessDeniedHandler())
                )

                // 사용자 조회 서비스
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
