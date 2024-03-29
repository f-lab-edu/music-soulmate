package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.exception.DuplicateMemberException;
import com.flab.musolmate.member.web.request.MemberRegisterRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.flab.musolmate.common.exception.ExceptionEnum.MESSAGE_DUPLICATE_MEMBER_EXCEPTION_EMAIL;
import static com.flab.musolmate.common.exception.ExceptionEnum.MESSAGE_DUPLICATE_MEMBER_EXCEPTION_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class MemberBasicServiceTest {

    @Autowired MemberBasicService memberBasicService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    MemberRegisterRequest requestDto;

    @BeforeEach
    public void setup() {
        String email = "aaa@gmail.com";
        String password = "1234qwer";
        String nickName = "aaa";

        requestDto = MemberRegisterRequest.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build();
    }

    @AfterEach
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void 회원등록(){
        // given
        String email = "aaa@gmail.com";
        String password = "1234qwer";
        String nickName = "aaa";

        // when
        Member registeredMember = memberBasicService.registerMember( requestDto );
        System.out.println( "registeredMember.getPassword() = " + registeredMember.getEncodedPassword() );

        // then
        assertThat( registeredMember.getEmail() ).isEqualTo( email );
        assertThat( passwordEncoder.matches( password, registeredMember.getEncodedPassword() ) ).isTrue();
        assertThat( registeredMember.getEncodedPassword().length() ).isGreaterThan( 10 );
        assertThat( registeredMember.getNickName() ).isEqualTo( nickName );

    }

    @Test
    public void 이메일_중복검사() {
        // given
        System.out.println( "requestDto > email = " + requestDto.getEmail() );
        Member registeredMember = memberBasicService.registerMember( requestDto );
        assertThat( registeredMember.getId() ).isNotNull();
        assertThat( registeredMember.getEmail() ).isEqualTo( requestDto.getEmail() );

        // when
        assertThatThrownBy( () -> memberBasicService.registerMember( requestDto ) )
            .isInstanceOf( DuplicateMemberException.class )
            .hasMessageContaining( MESSAGE_DUPLICATE_MEMBER_EXCEPTION_EMAIL );

    }

    @Test
    public void 닉네임_중복검사() {
        // given
        System.out.println( "requestDto > nickName = " + requestDto.getNickName() );
        Member registeredMember = memberBasicService.registerMember( requestDto );
        assertThat( registeredMember.getId() ).isNotNull();
        assertThat( registeredMember.getNickName() ).isEqualTo( requestDto.getNickName() );

        // when
        MemberRegisterRequest requestDto2 = MemberRegisterRequest.builder()
            .email( "bbb@gmail.com" )
            .password( "1234qwer" )
            .nickName( requestDto.getNickName() )
            .build();

        // then
        assertThatThrownBy( () -> memberBasicService.registerMember( requestDto2 ) )
            .isInstanceOf( DuplicateMemberException.class )
            .hasMessageContaining( MESSAGE_DUPLICATE_MEMBER_EXCEPTION_NICKNAME );

    }

    @Test
    @DisplayName( "Member의 id로 권한 정보를 포함한 회원 정보를 조회할 수 있다" )
    public void 회원정보_조회(){
        // given
        Member registeredMember = memberBasicService.registerMember( requestDto );
        assertThat( registeredMember.getId() ).isNotNull();
        assertThat( registeredMember.getEmail() ).isEqualTo( requestDto.getEmail() );

        // when
        Member member = memberBasicService.getMemberWithAuthorities( registeredMember.getId() ).get();

        // then
        assertThat( member.getId() ).isEqualTo( registeredMember.getId() );
        assertThat( member.getEmail() ).isEqualTo( registeredMember.getEmail() );
        assertThat( member.getEncodedPassword() ).isEqualTo( registeredMember.getEncodedPassword() );
        assertThat( member.getNickName() ).isEqualTo( registeredMember.getNickName() );
    }

}