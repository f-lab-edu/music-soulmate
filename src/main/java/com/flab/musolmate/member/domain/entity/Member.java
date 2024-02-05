package com.flab.musolmate.member.domain.entity;

import com.flab.musolmate.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "member_id", nullable = false )
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @Column( name = "password", nullable = false )
    private String encodedPassword;

    private String nickName;

    @ManyToMany
    @JoinTable(
        name = "member_authority",
        joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

// TODO. 음악 엔티티와 연결 필요
//    private ArrayList<Artist> favoriteArtistList;
//    private ArrayList<Genre> favoriteGenreList;

    @Builder
    public Member( String email, String password, String nickName, Set<Authority> authorities ) {
        this.email = email;
        this.encodedPassword = password;
        this.nickName = nickName;
        this.authorities = authorities;
    }

}
