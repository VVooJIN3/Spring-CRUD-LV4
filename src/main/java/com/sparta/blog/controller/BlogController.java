package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;
    HttpServletResponse response;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/blogs")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BlogResponseDto responseDto =blogService.createBlog(requestDto,userDetails.getUser());
        return responseDto;
    }

    @GetMapping("/blogs")
    public List<BlogResponseDto> getBlogs() {
        List<BlogResponseDto> responseDtos =blogService.getBlogs();
        return responseDtos;
    }

    @GetMapping("/blogs/{id}")
    public BlogResponseDto getBlog(@PathVariable Long id) {
        BlogResponseDto responseDto = blogService.getBlog(id);
        return responseDto;
    }

    @PutMapping("/blogs/{id}")
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BlogResponseDto responseDto = blogService.updateBlog(id, requestDto,userDetails.getUser());
        return responseDto;

    }

    @DeleteMapping("/blogs/{id}")
    public String deleteBlog(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        blogService.deleteBlog(id,userDetails.getUser());
        return "삭제 완료되었습니다.";

    }
}
