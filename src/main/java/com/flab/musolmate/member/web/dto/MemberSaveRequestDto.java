package com.flab.musolmate.member.web.dto;

import com.flab.musolmate.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {
    private String email;
    private String password;
    private String nickName;

    @Builder
    public MemberSaveRequestDto(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .build();
    }
}
