package com.flab.musolmate.common.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * UsernamePasswordAuthenticationFilter 앞에 커스텀한 JwtFilter를 추가하기 위한 클래스
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter< DefaultSecurityFilterChain, HttpSecurity > {
    private final TokenProvider tokenProvider;

    public JwtSecurityConfig( TokenProvider tokenProvider ) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure( HttpSecurity http ) throws Exception {
        JwtFilter customFilter = new JwtFilter( tokenProvider );
        http.addFilterBefore( customFilter, UsernamePasswordAuthenticationFilter.class );
    }
}
