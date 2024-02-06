package com.flab.musolmate.member.domain.entity;

import com.flab.musolmate.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member extends BaseTimeEntity implements UserDetails {

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

    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities() {
        return authorities.stream()
            .map( authority -> new SimpleGrantedAuthority( authority.getAuthorityName() ) )
            .collect( Collectors.toList() );
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
