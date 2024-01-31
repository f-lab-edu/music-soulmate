package com.flab.musolmate.common.security;

import com.flab.musolmate.member.web.response.LoginResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증
 *
 */
@Slf4j
@Component
public class TokenProvider{
    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static final String AUTHORITIES_KEY = "auth";
    private final long tokenValidityInMilliseconds;

    private Key key;

    public TokenProvider( @Value( "${jwt.token-validity-in-seconds}" ) long tokenValidityInSeconds ) {
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.key = Keys.secretKeyFor( SignatureAlgorithm.HS512 );
    }

    /**
     * Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 메소드
     * @param authentication
     * @return
     */
    public String createTokenString( Authentication authentication) {
        String authorities = getAuthorities( authentication );

        Date validity = getTokenExpirationDate();

        return createToken( authentication, authorities, validity );
    }

    /**
     * Authentication 객체의 권한정보를 받아 로그인 응답 객체 생성 (token, token 만료시간)
     * @param authentication
     * @return
     */
    public LoginResponse createLoginResponse( Authentication authentication ) {
        String authorities = getAuthorities( authentication );
        Date validity = getTokenExpirationDate();
        String token = createToken( authentication, authorities, validity );
        return new LoginResponse( token, validity.getTime() );
    }

    /**
     * Token에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드
     * @param token
     * @return
     */
    public Authentication getAuthentication( String token ) {
        Claims claims = getClaimsFromToken( token );

        Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromClaims( claims );

        User principal = new User( claims.getSubject(), "", authorities );

        return new UsernamePasswordAuthenticationToken( principal, token, authorities ); // 유저정보, 토큰, 권한정보 => Authentication 객체 생성
    }

    public boolean validateToken( String token ) {
        try {
            Jwts.parser().setSigningKey( key ).build().parseClaimsJws( token );
            return true;
        }
        catch ( SecurityException | MalformedJwtException e ) {
            logger.error( "잘못된 JWT 서명입니다." );
        }
        catch ( ExpiredJwtException e ) {
            logger.error( "만료된 JWT 토큰입니다." );
        }
        catch ( IllegalArgumentException e ) {
            logger.error( "JWT 토큰이 잘못되었습니다." );
        }
        return false;
    }


    private String getAuthorities( Authentication authentication ) {
        return authentication.getAuthorities().stream()
            .map( GrantedAuthority::getAuthority )
            .collect( Collectors.joining( "," ) );
    }

    /**
     * 토큰 만료시간 = 토큰을 생성한 시간 + 토큰 유효시간
     * @return
     */
    private Date getTokenExpirationDate() {
        long now = ( System.currentTimeMillis() );
        return new Date( now + this.tokenValidityInMilliseconds );
    }

    private String createToken( Authentication authentication, String authorities, Date validity ) {
        return Jwts.builder()
            .subject( authentication.getName() ) // setSubject deprecated
            .claim( "authorities", authorities )
            .signWith( key, SignatureAlgorithm.HS512 )
            .expiration( validity ) // setExpiration deprecated
            .compact();
    }


    private Claims getClaimsFromToken( String token ) {
        return Jwts.parser()
            .setSigningKey( key )
            .build()
            .parseClaimsJws( token )
            .getBody();
    }

    private static List< SimpleGrantedAuthority > getAuthoritiesFromClaims( Claims claims ) {
        return Arrays.stream( claims.get( AUTHORITIES_KEY ).toString().split( "," ) )
            .map( SimpleGrantedAuthority::new )
            .collect( Collectors.toList() );
    }


}
