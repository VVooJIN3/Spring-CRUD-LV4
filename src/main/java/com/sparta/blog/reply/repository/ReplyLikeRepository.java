package com.sparta.blog.reply.repository;

import com.sparta.blog.reply.entity.Reply;
import com.sparta.blog.reply.entity.ReplyLike;
import com.sparta.blog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Optional<ReplyLike> findByReplyAndUser(Reply reply, User user);

}
