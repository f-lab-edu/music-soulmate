package com.flab.musolmate.member.web.dto;

import com.flab.musolmate.member.domain.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
public class MemberRegisterRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickName;

    @Builder
    public MemberRegisterRequest( String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
    public Member toEntity( String encodedPassword ) {
        return Member.builder()
            .email(email)
            .password(encodedPassword)
            .nickName(nickName)
            .build();
    }
}
