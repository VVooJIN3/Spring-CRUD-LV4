package com.sparta.blog.blog.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.blog.blog.BlogSearchCond;
import com.sparta.blog.blog.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.sparta.blog.blog.entity.QBlog.blog;

@RequiredArgsConstructor
public class BlogRepositoryQueryImpl implements BlogRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    //제목으로 검색하기
    @Override
    public Page<Blog> search(BlogSearchCond cond, Pageable pageable) {
        var query = jpaQueryFactory
                .selectFrom(blog)
                .where(
                        blog.title.contains(cond.getTitle())
                ).offset(pageable.getOffset()).limit(pageable.getPageSize());

        query.orderBy(blog.createdAt.desc());

        var blogs = query.fetch();
        var totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(blog)
                .where(
                        blog.title.contains(cond.getTitle())
                ).fetch().get(0);

        return PageableExecutionUtils.getPage(blogs, pageable, () -> totalSize);
    }
}
