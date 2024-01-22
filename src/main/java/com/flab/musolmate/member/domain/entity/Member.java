package com.flab.musolmate.member.domain.entity;

import com.flab.musolmate.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", nullable = false )
    private Long id;

    private String email;

    private String password;

    private String nickName;

// TODO. 음악 엔티티와 연결 필요
//    private ArrayList<Artist> favoriteArtistList;
//    private ArrayList<Genre> favoriteGenreList;

    @Builder
    public Member( String email, String password, String nickName ) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

}
