package com.flab.musolmate.member.web;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.service.MemberBasicService;
import com.flab.musolmate.member.web.request.MemberRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Member> registerMember( @Valid @RequestBody MemberRegisterRequest requestDto) {

        Member registeredMember = memberBasicService.registerMember( requestDto );
        return new ResponseEntity<>( registeredMember, HttpStatus.CREATED );
    }

    @GetMapping("/hello")
    @PreAuthorize( "hasAnyAuthority('ROLE_USER')" )
    public String hello() {
        return "hello";
    }


}
