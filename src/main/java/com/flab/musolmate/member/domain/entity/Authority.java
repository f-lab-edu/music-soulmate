package com.flab.musolmate.member.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table( name = "authority" )
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    @Builder
    public Authority( String authorityName ) {
        this.authorityName = authorityName;
    }

}