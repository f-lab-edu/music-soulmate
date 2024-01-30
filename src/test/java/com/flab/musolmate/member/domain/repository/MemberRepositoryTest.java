package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Authority;
import com.flab.musolmate.member.domain.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    Member memberA;
    Authority authUser;

    @BeforeEach
    public void setup() {
        authUser = Authority.builder()
            .authorityName( "ROLE_USER" )
            .build();

        memberA = Member.builder()
            .email( "aaa@gmail.com" )
            .password( "1234qwer" )
            .nickName( "aaa" )
            .authorities( Set.of( authUser ) )
            .build();
    }

    @AfterEach
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
        assertEquals( member.getEncodedPassword(), memberA.getEncodedPassword() );
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

    @Test
    public void email_기준으로_Member_조회() {
        // given
        memberRepository.save( memberA );
        authorityRepository.save( authUser );

        // when
        Member member = memberRepository.findOneWithAuthoritiesByEmail( memberA.getEmail() )
            .orElseThrow( () -> new IllegalArgumentException( "해당 유저가 없습니다." ) );

        // then
        assertThat( member.getEmail() ).isEqualTo( memberA.getEmail() );

        member.getAuthorities().forEach( authority -> {
            assertThat( authority.getAuthorityName() ).isEqualTo( authUser.getAuthorityName() );
        } );

    }

}