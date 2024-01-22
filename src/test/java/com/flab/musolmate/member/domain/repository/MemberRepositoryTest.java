package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Member;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith( SpringRunner.class )
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @After
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void 회원저장_불러오기() {
        // given
        String email = "aaa@gmail.com";
        String password = "1234";
        String nickName = "aaa";

        memberRepository.save( Member.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build() );

        // when
        List< Member > memberList = memberRepository.findAll();

        // then
        Member member = memberList.get( 0 );
        assertEquals( member.getEmail(), email );
        assertEquals( member.getPassword(), password );
        assertEquals( member.getNickName(), nickName );
    }
}