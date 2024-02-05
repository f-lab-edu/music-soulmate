package com.flab.musolmate.post.domain.entity;

import com.flab.musolmate.common.domain.BaseTimeEntity;
import com.flab.musolmate.member.domain.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * 회원이 작성하는 개별적인 게시물을 Post라 한다.
 * +) Feed는 여러 Post를 한 스트림으로 보여줄 때를 칭한다.
 */
@Getter
@Builder
@NoArgsConstructor
@Entity
@Table( name = "posts")
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "post_id", nullable = false )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotEmpty
    private String content;

//    private Music music; // TODO. 음악 링크 (옵션)

    @ColumnDefault( "false" )
    private boolean isPrivate; // 비공개 여부

    @ColumnDefault( "false" )
    private boolean isDeleted; // 삭제 여부

}
