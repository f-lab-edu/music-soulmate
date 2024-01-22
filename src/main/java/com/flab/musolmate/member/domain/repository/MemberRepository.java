package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository< Member, Long> {
}
