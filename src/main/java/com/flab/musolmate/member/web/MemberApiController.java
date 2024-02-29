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
    @PostMapping("/register")
    public ResponseEntity<Member> registerMember( @Valid @RequestBody MemberRegisterRequest requestDto) {

        Member registeredMember = memberBasicService.registerMember( requestDto );
        return new ResponseEntity<>( registeredMember, HttpStatus.CREATED );
    }

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority('ROLE_ADMIN')" )
    public ResponseEntity<Member> getMember( @PathVariable Long id ) {
        Member member = memberBasicService.getMemberWithAuthorities( id ).orElse( null );
        if ( member == null ) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND ); // TODO. 커스텀 예외 처리
        }
        return ResponseEntity.ok( member );
    }

    @GetMapping("/me")
    @PreAuthorize( "hasAnyAuthority('ROLE_USER')" )
    public ResponseEntity<Member> getMyMember() {
        return ResponseEntity.ok( memberBasicService.getMyMemberWithAthorities().get() );
    }
}
