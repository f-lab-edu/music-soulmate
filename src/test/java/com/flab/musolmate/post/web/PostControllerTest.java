package com.flab.musolmate.post.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.musolmate.common.SecurityConfig;
import com.flab.musolmate.common.security.JwtAccessDeniedHandler;
import com.flab.musolmate.common.security.JwtAuthenticationEntryPoint;
import com.flab.musolmate.common.security.TokenProvider;
import com.flab.musolmate.member.domain.entity.Authority;
import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.member.service.CustomUserDetailService;
import com.flab.musolmate.post.domain.repository.PostRepository;
import com.flab.musolmate.post.service.PostRegisterService;
import com.flab.musolmate.post.web.request.PostRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PostController.class)
@Import( SecurityConfig.class)
@MockBeans( {@MockBean(PostRegisterService.class),@MockBean(PostRepository.class), @MockBean( TokenProvider.class), @MockBean( JwtAuthenticationEntryPoint.class ), @MockBean( JwtAccessDeniedHandler.class ) })
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "userDetailsService")
    CustomUserDetailService customUserDetailService;
    static final String PATH_POST_REGISTER = "/posts/register";

    @BeforeEach
    public void setUp() {

        Authority authUser = Authority.builder()
            .authorityName( "ROLE_USER" )
            .build();

        Member memberUser = Member.builder()
            .email( "user@gmail.com" )
            .password( "1234qwer" )
            .nickName( "bbb" )
            .authorities( Set.of( authUser ) )
            .build();


        Authority authAdmin = Authority.builder()
            .authorityName( "ROLE_ADMIN" )
            .build();

        Member memberAdmin = Member.builder()
            .email( "admin@gmail.com" )
            .password( "1234qwer" )
            .nickName( "aaa" )
            .authorities( Set.of( authAdmin, authUser ) )
            .build();

        Authority authNone = Authority.builder()
            .authorityName( "ROLE_NONE" )
            .build();

        Member memberNoAuth = Member.builder()
            .email( "nono@gmail.com" )
            .password( "1234qwer" )
            .nickName( "aaa" )
            .authorities( Set.of( authNone ) )
            .build();

        Mockito.when(customUserDetailService.loadUserByUsername("user@gmail.com")).thenReturn(memberUser);
        Mockito.when(customUserDetailService.loadUserByUsername("admin@gmail.com")).thenReturn(memberAdmin);
        Mockito.when(customUserDetailService.loadUserByUsername("nono@gmail.com")).thenReturn(memberNoAuth);

    }

    @Test
    @DisplayName( "회원은 피드 게시물을 등록할 수 있다" )
    @WithUserDetails( setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "user@gmail.com", userDetailsServiceBeanName = "userDetailsService" )
    public void 피드_게시물_등록_성공() throws Exception {


        // given
        PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
            .content( "Test Content" )
            .isPrivate( true )
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContents = objectMapper.writeValueAsString( postRegisterRequest );

        // when / then
        mockMvc.perform(post( PATH_POST_REGISTER )
                .contentType( MediaType.APPLICATION_JSON)
                .content( jsonContents ))
            .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName( "권한이 없는 회원은 피드 게시물을 등록할 수 없다" )
    @WithUserDetails( setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "nono@gmail.com", userDetailsServiceBeanName = "userDetailsService" )
    public void 피드_게시물_등록_실패() throws Exception {


        // given
        PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
            .content( "Test Content" )
            .isPrivate( true )
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContents = objectMapper.writeValueAsString( postRegisterRequest );

        // when / then
        mockMvc.perform(post( PATH_POST_REGISTER )
                .contentType( MediaType.APPLICATION_JSON)
                .content( jsonContents ))
            .andExpect(status().isForbidden() )
        ;
    }


}