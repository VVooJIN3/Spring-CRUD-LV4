package com.sparta.blog.repository;

import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByOrderByModifiedAtDesc();


}

