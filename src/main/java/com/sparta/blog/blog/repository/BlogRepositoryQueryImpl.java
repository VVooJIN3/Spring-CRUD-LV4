package com.sparta.blog.blog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.blog.blog.BlogSearchCond;
import com.sparta.blog.blog.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sparta.blog.blog.entity.QBlog.blog;

@RequiredArgsConstructor
public class BlogRepositoryQueryImpl implements BlogRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    //제목으로 검색하기
    @Override
    public List<Blog> search(BlogSearchCond cond, Pageable pageable) {
        List<Blog> result = jpaQueryFactory
                .selectFrom(blog)
                .where(
                        blog.title.contains(cond.getTitle())
                ).fetch();
        return result;
    }
}
