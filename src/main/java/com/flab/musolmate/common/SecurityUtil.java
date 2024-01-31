package com.flab.musolmate.common;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger( SecurityUtil.class);

    /**
     * Security Context에서 현재 인증정보(Authentication 객체)를 가져와 email을 리턴하는 메소드
     * @return
     */
    public static Optional<String> getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null ) {
            logger.error( "Security Context에 인증 정보가 없습니다." );
            return Optional.empty();
        }

        String email = authentication.getName();
        if ( authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails ) {
            email = ( ( org.springframework.security.core.userdetails.UserDetails ) authentication.getPrincipal() ).getUsername();
        }
        else if ( authentication.getPrincipal() instanceof String ) {
            email = ( String ) authentication.getPrincipal();
        }
        return Optional.ofNullable( email );
    }
}
