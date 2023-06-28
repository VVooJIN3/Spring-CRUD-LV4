package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.entity.Blog;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<BlogResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    public BlogResponseDto createBlog(BlogRequestDto requestDto, User user) {
        Blog blog = new Blog(requestDto, user);
        Blog saveBlog = blogRepository.save(blog);
        BlogResponseDto blogResponseDto = new BlogResponseDto(blog);
        return blogResponseDto;
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        Blog updatedBlog;
        if (blog != null) {
            if(user.getUsername().equals(blog.getUsername())) {
                blog.update(requestDto, user);
                updatedBlog = blog;
            }
            else{
                throw new IllegalArgumentException("로그인한 작성자의 게시물이 아닙니다.");
            }
            return new BlogResponseDto(updatedBlog);
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다. 수정할 수 없습니다.");
        }
    }

    public Long deleteBlog(Long id, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog =findBlog(id);
        if (blog != null) {
            if(user.getUsername().equals(blog.getUsername())) {
                blogRepository.delete(blog);
            }else{
                throw new IllegalArgumentException("로그인한 작성자의 게시물이 아닙니다. 삭제할 수 없습니다.");
            }

            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public BlogResponseDto getBlog(Long id) {
        Blog blog = findBlog(id);
        if (blog != null) {

            return new BlogResponseDto(blog);
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }

    }

    private Blog findBlog(Long id){
        return blogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}
