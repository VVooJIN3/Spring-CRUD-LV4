package com.sparta.blog.blog.repository;

import com.sparta.blog.blog.BlogSearchCond;
import com.sparta.blog.blog.entity.Blog;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogRepositoryQuery {
    List<Blog> search(BlogSearchCond cond, Pageable pageable);
}
