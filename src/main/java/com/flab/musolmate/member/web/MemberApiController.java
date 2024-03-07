package com.flab.musolmate.member.web;

import com.flab.musolmate.common.domain.api.ApiResponse;
import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.exception.NotFoundMemberException;
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
@RequestMapping( "/members" )
public class MemberApiController {
    private final MemberBasicService memberBasicService;

    /**
     * 회원가입
     *
     * @param requestDto
     * @return
     */
    @PostMapping( "/register" )
    public ResponseEntity< ApiResponse< Member > > registerMember( @Valid @RequestBody MemberRegisterRequest requestDto ) {

        Member registeredMember = memberBasicService.registerMember( requestDto );
        return ResponseEntity.status( HttpStatus.CREATED ).body( ApiResponse.createSuccess( registeredMember ) );
    }

    @GetMapping( "/{id}" )
    @PreAuthorize( "hasAnyAuthority('ROLE_ADMIN')" )
    public ResponseEntity< ApiResponse< Member > > getMember( @PathVariable Long id ) {
        Member member = memberBasicService.getMemberWithAuthorities( id ).orElseThrow( () -> new NotFoundMemberException( "회원 정보가 존재하지 않습니다." ) );
        return ResponseEntity.status( HttpStatus.OK ).body( ApiResponse.createSuccess( member ) );
    }

    @GetMapping( "/me" )
    @PreAuthorize( "hasAnyAuthority('ROLE_USER')" )
    public ResponseEntity< ApiResponse< Member > > getMyMember() {
        Member member = memberBasicService.getMyMemberWithAthorities().orElseThrow( () -> new NotFoundMemberException( "회원 정보가 존재하지 않습니다." ) );
        return ResponseEntity.status( HttpStatus.OK ).body( ApiResponse.createSuccess( member ) );
    }
}
