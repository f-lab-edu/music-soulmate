package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.exception.DuplicateMemberException;
import com.flab.musolmate.member.web.dto.MemberRegisterRequest;
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
        // 이메일 중복 검사
        if ( memberRepository.existsByEmail( requestDto.getEmail() ) ) {
            throw new DuplicateMemberException( "이미 존재하는 이메일입니다." );
        }
        // 닉네임 중복 검사
        if ( memberRepository.existsByNickName( requestDto.getNickName() ) ) {
            throw new DuplicateMemberException( "이미 존재하는 닉네임입니다." );
        }

        // 비밀번호 암호화 + save
        return memberRepository.save( requestDto.toEntity( passwordEncoder ) );
    }
}
