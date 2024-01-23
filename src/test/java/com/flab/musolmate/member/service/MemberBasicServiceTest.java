package com.flab.musolmate.member.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith( SpringRunner.class )
@SpringBootTest
class MemberBasicServiceTest {

    @Autowired MemberBasicService memberBasicService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @After
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
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build();

        Member registeredMember = memberBasicService.registerMember( requestDto );
        System.out.println( "registeredMember.getPassword() = " + registeredMember.getPassword() );

        // then
        assertThat( registeredMember.getEmail() ).isEqualTo( email );
        assertThat( passwordEncoder.matches( password, registeredMember.getPassword() ) ).isTrue();
        assertThat( registeredMember.getPassword().length() ).isGreaterThan( 10 );
        assertThat( registeredMember.getNickName() ).isEqualTo( nickName );

    }

}