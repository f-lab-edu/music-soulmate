package com.flab.musolmate.member.web;

import com.flab.musolmate.member.service.MemberBasicService;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberBasicService memberBasicService;

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @PostMapping("/member")
    public Long registerMember( @RequestBody MemberSaveRequestDto requestDto ) {
        return memberBasicService.registerMember( requestDto );
    }


}
