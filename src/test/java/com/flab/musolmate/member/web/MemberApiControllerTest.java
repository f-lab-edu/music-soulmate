package com.flab.musolmate.member.web;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.domain.repository.MemberRepository;
import com.flab.musolmate.member.web.dto.MemberSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class MemberApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @After
    public void tearDown() throws Exception {
        memberRepository.deleteAll();
    }

    @Test
    public void member_등록성공() throws Exception {
        // given
        String email = "aaa@gmail.com";
        String password = "1234";
        String nickName = "aaa";
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
            .email( email )
            .password( password )
            .nickName( nickName )
            .build();

        String url = "http://localhost:" + port + "/member";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity( url, requestDto, Long.class );

        // then
        assertThat( responseEntity.getStatusCode() ).isEqualTo( HttpStatus.OK );
        assertThat( responseEntity.getBody() ).isGreaterThan( 0L );

        List< Member > all = memberRepository.findAll();
        assertThat( all.get( 0 ).getEmail() ).isEqualTo( email );
        assertThat( all.get( 0 ).getPassword() ).isEqualTo( password );
        assertThat( all.get( 0 ).getNickName() ).isEqualTo( nickName );
    }

}