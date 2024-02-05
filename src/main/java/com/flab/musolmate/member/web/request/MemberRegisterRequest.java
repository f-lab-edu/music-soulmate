package com.flab.musolmate.member.web.request;

import com.flab.musolmate.member.domain.entity.Authority;
import com.flab.musolmate.member.domain.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
public class MemberRegisterRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // TODO. 왜 junit으로 가입시 비밀번호가 null로 들어가는지 확인
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
    public Member toEntity( String encodedPassword, Set< Authority > authorities ) {
        return Member.builder()
            .email(email)
            .password(encodedPassword)
            .nickName(nickName)
            .authorities( authorities )
            .build();
    }
}
