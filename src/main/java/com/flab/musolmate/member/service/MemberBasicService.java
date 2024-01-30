package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.exception.DuplicateMemberException;
import com.flab.musolmate.member.web.request.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberBasicService {

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

        return memberRepository.save( requestDto.toEntity( passwordEncoder.encode( requestDto.getPassword() ) ) );
    }

    private void checkDuplicateEmailAndNickName( MemberRegisterRequest requestDto ) {

        if ( memberRepository.existsByEmail( requestDto.getEmail() ) ) {
            throw new DuplicateMemberException( "이미 존재하는 이메일입니다." );
        }
        if ( memberRepository.existsByNickName( requestDto.getNickName() ) ) {
            throw new DuplicateMemberException( "이미 존재하는 닉네임입니다." );
        }
    }
}
