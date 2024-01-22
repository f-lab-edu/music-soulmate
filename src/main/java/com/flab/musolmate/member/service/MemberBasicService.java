package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberBasicService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @Transactional
    public Long registerMember( MemberSaveRequestDto requestDto ) {
        return memberRepository.save( requestDto.toEntity() ).getId();
    }
}
