package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith( SpringRunner.class )
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    Member memberA;

    @Before
    public void setup() {
        memberA = Member.builder()
            .email( "aaa@gmail.com" )
            .password( "1234qwer" )
            .nickName( "aaa" )
            .build();
    }

    @After
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void 회원저장_불러오기() {
        // given
        memberRepository.save( memberA );

        // when
        List< Member > memberList = memberRepository.findAll();

        // then
        Member member = memberList.get( 0 );
        assertEquals( member.getEmail(), memberA.getEmail() );
        assertEquals( member.getPassword(), memberA.getPassword() );
        assertEquals( member.getNickName(), memberA.getNickName() );
    }

    @Test
    public void 이메일_중복검사(){
        // given
        memberRepository.save( memberA );

        // when
        boolean isDuplicated = memberRepository.existsByEmail( memberA.getEmail() );

        // then
        assertTrue( isDuplicated );
    }

    @Test
    public void 닉네임_중복검사(){
        // given
        memberRepository.save( memberA );

        // when
        boolean isDuplicated = memberRepository.existsByNickName( memberA.getNickName() );

        // then
        assertTrue( isDuplicated );
    }

    @Test
    public void BaseTimeEntity_등록() {
        // given
        LocalDateTime now = LocalDateTime.of( 2024, 1, 21, 0, 0, 0 );
        memberRepository.save( memberA );

        // when
        List< Member > memberList = memberRepository.findAll();

        // then
        Member member = memberList.get( 0 );
        System.out.println( ">>>>>>>>> createDate=" + member.getCreatedDate() + ", modifiedDate=" + member.getModifiedDate() );
        assertThat( member.getCreatedDate() ).isAfter( now );
        assertThat( member.getModifiedDate() ).isAfter( now );
    }

}