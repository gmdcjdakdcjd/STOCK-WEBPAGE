package com.stock.webpage.config;

import com.stock.webpage.security.CustomUserDetailsService;
import com.stock.webpage.security.handler.Custom403Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("------------ security configure -------------------");

        /* =========================
           로그인
           ========================= */
        http.formLogin(login -> login
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .defaultSuccessUrl("/", false)
                .failureUrl("/member/login?error=true")
        );

        /* =========================
           로그아웃
           ========================= */
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        /* =========================
           접근 권한
           ========================= */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/board/**",
                        "/mystock/**",
                        "/member/**",
                        "/css/**",
                        "/js/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()
                .anyRequest().permitAll()
        );

        /* =========================
           CSRF (개발 단계)
           ========================= */
        http.csrf(csrf -> csrf.disable());

        /* =========================
           Remember-Me
           ========================= */
        http.rememberMe(rememberMe -> rememberMe
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60 * 60 * 24 * 30)
        );

        /* =========================
           접근 거부 처리
           ========================= */
        http.exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler())
        );

        return http.build();
    }

    /* =========================
       403 Handler
       ========================= */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    /* =========================
       Static Resource Ignore
       ========================= */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------ web ignore configure -------------------");

        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /* =========================
       Remember-Me Token Repo
       ========================= */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
