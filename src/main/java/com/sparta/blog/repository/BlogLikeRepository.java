package com.sparta.blog.repository;

import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.BlogLike;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BlogLikeRepository  extends JpaRepository<BlogLike, Long> {
    Optional<BlogLike> findByBlogAndUser(Blog blog,User user);

}

