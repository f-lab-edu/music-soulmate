package com.flab.musolmate.post.domain.repository;

import com.flab.musolmate.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository< Post, Long> {
}
