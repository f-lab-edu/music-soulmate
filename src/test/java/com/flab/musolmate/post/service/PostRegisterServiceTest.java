package com.flab.musolmate.post.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.post.domain.repository.PostRepository;
import com.flab.musolmate.post.web.request.PostRegisterRequest;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRegisterServiceTest {
    @Autowired PostRegisterService postRegisterService;
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName( "모든 회원은 게시글을 등록할 수 있다" )
    public void 게시글_등록_성공() {
        // given
        Member member = getSampleMember();

        String content = "게시글 내용";
        boolean isPrivate = false;

        PostRegisterRequest requestDto = PostRegisterRequest.builder()
            .content( content )
            .isPrivate( isPrivate )
            .build();


        // when
        postRegisterService.registerPost( member, requestDto );

        // then
        assertThat( postRepository.findAll() ).hasSize( 1 );
        assertThat( postRepository.findAll().get( 0 ).getContent() ).isEqualTo( content );
        assertThat( postRepository.findAll().get( 0 ).isPrivate() ).isEqualTo( isPrivate );
        assertThat( postRepository.findAll().get( 0 ).getMember().getId() ).isEqualTo( member.getId() );
    }

    @Test
    @DisplayName( "빈 게시글은 등록할 수 없다" )
    public void 게시글_등록_실패() {
        // given
        Member member = getSampleMember();

        boolean isPrivate = false;

        PostRegisterRequest requestDto = PostRegisterRequest.builder()
            .content( null )
            .isPrivate( isPrivate )
            .build();


        // when -> then: error
        Assertions.assertThatThrownBy( () -> postRegisterService.registerPost( member, requestDto ) )
            .isInstanceOf( ConstraintViolationException.class );
    }

    private Member getSampleMember() {
        Member member =  Member.builder()
            .email( "aaa@gmail.com" )
            .password( "1234qwer" )
            .nickName( "aaa" )
            .build();

        memberRepository.save( member );

        return member;
    }
}