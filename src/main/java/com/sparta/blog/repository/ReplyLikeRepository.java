package com.sparta.blog.repository;

import com.sparta.blog.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike,Long> {
    Optional<ReplyLike> findByReplyAndUser(Reply reply, User user);

}
