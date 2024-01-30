package com.flab.musolmate.common;

import com.flab.musolmate.common.security.JwtAccessDeniedHandler;
import com.flab.musolmate.common.security.JwtAuthenticationEntryPoint;
import com.flab.musolmate.common.security.JwtSecurityConfig;
import com.flab.musolmate.common.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) //@EnableGlobalMethodSecurity 는 deprecated 되었다. @PreAuthorize, @PostAuthorize 등을 사용하기 위해 필요
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화

            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 인증 실패 처리
                .accessDeniedHandler(jwtAccessDeniedHandler)            // 인가 실패 처리
            )

            .headers(headers -> headers
                .frameOptions().sameOrigin() // 클릭재킹 방지 설정
            )

            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 정책을 STATELESS로 설정
            )

            /* TODO. 로그인 API와 회원가입 API는 인증에서 제외 */
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/members/**").permitAll() // /members/** 경로는 인증 없이 접근 허용
                .anyRequest().authenticated() // 그 외 경로는 인증 필요
            )

            .apply( new JwtSecurityConfig(tokenProvider) ); // jwtFilter를 addFilterBefore로 등록했던 Configurer를 적용

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

