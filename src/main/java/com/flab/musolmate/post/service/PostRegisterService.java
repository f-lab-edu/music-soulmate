package com.flab.musolmate.post.service;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.post.domain.entity.Post;
import com.flab.musolmate.post.domain.repository.PostRepository;
import com.flab.musolmate.post.web.request.PostRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRegisterService {
    private final PostRepository postRepository;

    public void registerPost( PostRegisterRequest postRequest, Member member ) {
        Post post = postRequest.toEntity( member );
        postRepository.save( post );
    }
}
