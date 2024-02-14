package com.flab.musolmate.post.web;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.post.service.PostRegisterService;
import com.flab.musolmate.post.web.request.PostRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostRegisterService postRegisterService;

    /**
     * 피드 게시글 등록
     */
    @PostMapping("/register")
    @PreAuthorize( "hasAnyAuthority('ROLE_USER')" )
    public ResponseEntity< HttpStatus > registerPost( @AuthenticationPrincipal Member member, @Valid @RequestBody PostRegisterRequest requestDto) {

        postRegisterService.registerPost( member, requestDto );

        return ResponseEntity.status( HttpStatus.CREATED ).build();
    }
}
