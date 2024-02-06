package com.flab.musolmate.post.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.musolmate.common.SecurityConfig;
import com.flab.musolmate.common.security.JwtAccessDeniedHandler;
import com.flab.musolmate.common.security.JwtAuthenticationEntryPoint;
import com.flab.musolmate.common.security.TokenProvider;
import com.flab.musolmate.post.service.PostRegisterService;
import com.flab.musolmate.post.web.request.PostRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PostController.class)
@Import( SecurityConfig.class)
@MockBeans( {@MockBean(PostRegisterService.class), @MockBean( TokenProvider.class), @MockBean( JwtAuthenticationEntryPoint.class ), @MockBean( JwtAccessDeniedHandler.class ) })
class PostControllerTest {

   @Autowired
    MockMvc mockMvc;

    static final String PATH_POST_BASE = "/posts";
    static final String PATH_POST_REGISTER = "/register";

    @Test
    @DisplayName( "회원은 피드 게시물을 등록할 수 있다" )
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"}, password = "test1234")
    public void 피드_게시물_등록_성공() throws Exception {

        // given
        PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
            .content( "Test Content" )
            .isPrivate( true )
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContents = objectMapper.writeValueAsString( postRegisterRequest );

        // when
        mockMvc.perform(post( PATH_POST_BASE + PATH_POST_REGISTER )
                .contentType( MediaType.APPLICATION_JSON)
                .content( jsonContents ))
            .andExpect(status().isCreated())
            .andExpect( result -> {
                System.out.println( "result = " + result );
            })
        ;

        // then


    }


}