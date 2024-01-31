package com.flab.musolmate.member.web;

import com.flab.musolmate.common.security.TokenProvider;
import com.flab.musolmate.member.web.request.LoginRequest;
import com.flab.musolmate.member.web.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    // 로그인 성공시 토큰 발급
    private final TokenProvider tokenProvider;
    // 유저 정보를 가져오기 위한 AuthenticationManagerBuilder
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity< LoginResponse > emailLogin( @Valid @RequestBody LoginRequest loginRequest ){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword() );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate( authenticationToken );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        LoginResponse response = tokenProvider.createLoginResponse( authentication );

        HttpHeaders headers = new HttpHeaders();
        headers.add( HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken() );

        return ResponseEntity.ok().headers( headers ).body( response );
    }
}
