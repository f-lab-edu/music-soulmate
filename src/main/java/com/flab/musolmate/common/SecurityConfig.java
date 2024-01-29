package com.flab.musolmate.common;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @EnableWebSecurity : 기본적인 웹 보안 활성화
 * WebSecurityConfigurerAdapter를 상속받아 configure()를 오버라이드 하는 방식이 deprecated 되었다.
 * (https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
 * -> 직접 @Bean으로 등록하여 설정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http) throws Exception {
        http
            .csrf( AbstractHttpConfigurer::disable )                // csrf 비활성화
            .authorizeHttpRequests( authz -> authz                  // http 접근 제한 설정
                .requestMatchers( "/members/**" ).permitAll()     // /members/** 경로는 모두 허용
                .anyRequest().authenticated()                       // 나머지는 인증 필요
            );
        return http.build();
    }

    /**
     * 403 Forbidden 에러가 발생하는 경우, 정적 자원에 대해서 인증을 거치지 않도록 설정
     * ( filterChain에서 permitAll()을 사용하면 filter까지 거치게 된다. ignore()를 사용하면 filter를 거치지 않아서 더 효율적이다. )
     * @return
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
            .requestMatchers( "/h2-console/**", "/favicon.ico" );
    }
}

