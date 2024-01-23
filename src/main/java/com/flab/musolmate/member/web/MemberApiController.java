package com.flab.musolmate.member.web;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.service.MemberBasicService;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberApiController {
    private final MemberBasicService memberBasicService;

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Member> registerMember( @Valid @RequestBody MemberSaveRequestDto requestDto) {

        Member registeredMember = memberBasicService.registerMember( requestDto );
        return ResponseEntity.status( HttpStatus.OK).body( registeredMember );
    }


}
