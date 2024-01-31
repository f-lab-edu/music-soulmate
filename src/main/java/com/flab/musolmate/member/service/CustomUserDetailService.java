package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component( "userDetailsService" )
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional // 왜 썼을까 알아보기 -> DB에서 Member 정보와 권한 정보 두가지 모두 가져와야 하기 때문에 트랜잭션 사용
    @Override // parameter는 왜 final을 추가했을까 -> 변수에 final을 붙이면 재할당이 불가하다. 로그인에 사용되는 중요한 정보이기 때문에 재할당이 불가하도록 final을 붙인 것 같다.
    public UserDetails loadUserByUsername( final String email ) {
        return memberRepository.findOneWithAuthoritiesByEmail( email )
                                .map( member -> createMember( email, member) )
                                .orElseThrow( () -> new UsernameNotFoundException( email + " -> 데이터베이스에서 찾을 수 없습니다." ) );
    }

    private org.springframework.security.core.userdetails.User createMember(String email, Member member) {
        List< GrantedAuthority > grantedAuthorities = member.getAuthorities().stream()
                                                            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                                                            .collect( Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getEmail(),
                                                                    member.getEncodedPassword(),
                                                                    grantedAuthorities);
    }
}
