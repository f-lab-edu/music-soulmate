package com.flab.musolmate.member.domain.repository;

import com.flab.musolmate.member.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository< Authority, String> {
}
