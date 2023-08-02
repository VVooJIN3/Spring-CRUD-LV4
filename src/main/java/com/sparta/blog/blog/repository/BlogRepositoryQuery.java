package com.sparta.blog.blog.repository;

import com.sparta.blog.blog.BlogSearchCond;
import com.sparta.blog.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogRepositoryQuery {
    Page<Blog> search(BlogSearchCond cond, Pageable pageable);
}
