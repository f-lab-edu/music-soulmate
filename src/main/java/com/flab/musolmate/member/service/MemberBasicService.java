package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
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
    public Member registerMember( MemberSaveRequestDto requestDto ) {
        // 비밀번호 암호화
        return memberRepository.save( requestDto.toEntity( passwordEncoder ) );
    }
}
