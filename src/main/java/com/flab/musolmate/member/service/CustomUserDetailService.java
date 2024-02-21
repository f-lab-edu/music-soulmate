package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component( "userDetailsService" )
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public Member loadUserByUsername( final String email ) {
        return memberRepository.findOneWithAuthoritiesByEmail( email )
                                .orElseThrow( () -> new UsernameNotFoundException( email + " -> 데이터베이스에서 찾을 수 없습니다." ) );
    }

}
