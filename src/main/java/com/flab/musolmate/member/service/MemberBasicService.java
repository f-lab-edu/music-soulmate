package com.flab.musolmate.member.service;

import com.flab.musolmate.common.SecurityUtil;
import com.flab.musolmate.member.domain.entity.Authority;
import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.exception.DuplicateMemberException;
import com.flab.musolmate.member.web.request.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.flab.musolmate.common.exception.ExceptionEnum.MESSAGE_DUPLICATE_MEMBER_EXCEPTION_EMAIL;
import static com.flab.musolmate.common.exception.ExceptionEnum.MESSAGE_DUPLICATE_MEMBER_EXCEPTION_NICKNAME;

@RequiredArgsConstructor
@Service
public class MemberBasicService {
    public static final String AUTHORITY_NAME_USER = "ROLE_USER"; // TODO. enum으로 변경
    public static final String AUTHORITY_NAME_ADMIN = "ROLE_ADMIN"; // TODO. enum으로 변경

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @Transactional
    public Member registerMember( MemberRegisterRequest requestDto ) {
        /* 클라이언트에서 하겠지만 서버에서 한번 더 체크 */
        checkDuplicateEmailAndNickName( requestDto );

        Authority userAuthority = makeUserAuthority();

        return memberRepository.save( requestDto.toEntity( passwordEncoder.encode( requestDto.getPassword() ), Collections.singleton( userAuthority ) ) );
    }


    /**
     * Member의 id로 회원 정보와 해당 회원의 권한 정보 조회
     * @param id
     * @return
     */
    @Transactional(readOnly = true) // 성능 향상을 위해 readOnly = true로 설정한다는데 정말 큰 차이가 있을까?
    public Optional<Member> getMemberWithAuthorities( Long id ) {
        return memberRepository.findOneWithAuthoritiesById( id );
    }

    /**
     * 현재 SecurityContext에 저장된 회원 정보와 해당 회원의 권한 정보 조회
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Member> getMyMemberWithAthorities() {
        return SecurityUtil.getCurrentUserEmail().flatMap( memberRepository::findOneWithAuthoritiesByEmail );
    }

    private void checkDuplicateEmailAndNickName( MemberRegisterRequest requestDto ) {

        if ( memberRepository.findOneWithAuthoritiesByEmail( requestDto.getEmail() ).orElse( null ) != null ) {
            throw new DuplicateMemberException( MESSAGE_DUPLICATE_MEMBER_EXCEPTION_EMAIL );
        }
        if ( memberRepository.existsByNickName( requestDto.getNickName() ) ) {
            throw new DuplicateMemberException( MESSAGE_DUPLICATE_MEMBER_EXCEPTION_NICKNAME );
        }
    }

    private Authority makeUserAuthority() {

        return Authority.builder()
            .authorityName( AUTHORITY_NAME_USER )
            .build();
    }
}
