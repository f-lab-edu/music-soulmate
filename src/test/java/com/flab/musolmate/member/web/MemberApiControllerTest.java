package com.flab.musolmate.member.web;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.web.request.MemberRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class MemberApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void tearDown() throws Exception {
        memberRepository.deleteAll();
    }
    @Test
    public void member_등록실패() throws Exception {
        // given
        String email = "aa";
        String password = "1234";
        String nickName = "a";
        MemberRegisterRequest requestDto = MemberRegisterRequest.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build();

        String url = "http://localhost:" + port + "/members";

        // when
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity( url, requestDto, Object.class );

        // then
        assertThat( responseEntity.getStatusCode().is4xxClientError() );
        System.out.println( responseEntity.getBody() );

    }
    @Test
    public void member_등록성공() throws Exception {
        // given
        String email = "aaa@gmail.com";
        String password = "1234qwer";
        String nickName = "aaa";
        MemberRegisterRequest requestDto = MemberRegisterRequest.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build();

        String url = "http://localhost:" + port + "/members";

        // when
        ResponseEntity<Member> responseEntity = restTemplate.postForEntity( url, requestDto, Member.class );

        // then
        assertThat( responseEntity.getStatusCode() ).isEqualTo( HttpStatus.CREATED );

        memberRepository.findOneWithAuthoritiesByEmail( email )
            .ifPresent(
                member -> {
                    assertThat( member.getEmail() ).isEqualTo( email );
                    assertThat( passwordEncoder.matches( password, member.getEncodedPassword() ) ).isTrue();
                    assertThat( member.getNickName() ).isEqualTo( nickName );

                    member.getAuthorities()
                        .forEach( authority -> {
                            assertThat( authority.getAuthority() ).isEqualTo( "ROLE_USER" );
                        });
                    });

    }

}