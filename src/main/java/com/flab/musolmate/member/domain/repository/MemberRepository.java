package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository< Member, Long> {
    boolean existsByEmail( String email );

    boolean existsByNickName( String nickName );

    /**
     * email 기준으로 Member 조회. 권한 정보도 같이 조회
     * @param email
     * @return
     */
    @EntityGraph(attributePaths = "authorities") // Eager 조회로 쿼리 수행
    Optional<Member> findOneWithAuthoritiesByEmail( String email );

    @EntityGraph(attributePaths = "authorities") // Eager 조회로 쿼리 수행
    Optional<Member> findOneWithAuthoritiesById( Long id );
}
