package com.sparta.blog.blog.repository;

import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.blog.entity.BlogLike;
import com.sparta.blog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BlogLikeRepository  extends JpaRepository<BlogLike, Long> {
    Optional<BlogLike> findByBlogAndUser(Blog blog, User user);

}

