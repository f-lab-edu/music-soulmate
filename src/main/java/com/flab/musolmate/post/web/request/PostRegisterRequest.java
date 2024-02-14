package com.flab.musolmate.post.web.request;

import com.flab.musolmate.member.domain.entity.Member;
import com.flab.musolmate.post.domain.entity.Post;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class PostRegisterRequest {

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

//    private Music music; // TODO. 음악 링크 (옵션)

    private boolean isPrivate; // 비공개 여부

    public Post toEntity( Member member ) {
        return Post.builder()
            .member(member)
            .content(content)
            .isPrivate(isPrivate)
            .isDeleted( false )
            .build();
    }
}
