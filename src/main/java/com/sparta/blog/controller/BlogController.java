package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.ReplyRequestDto;
import com.sparta.blog.dto.ReplyResponseDto;
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
        return new BlogResponseDto(blogService.createBlog(requestDto,userDetails.getUser()));
    }

    @GetMapping("/blogs")
    public List<BlogResponseDto> getBlogs() {
        return blogService.getBlogs();
    }

    @GetMapping("/blogs/{blogId}")
    public BlogResponseDto getBlog(@PathVariable Long blogId) {
        return new BlogResponseDto(blogService.getBlog(blogId));
    }

    @PutMapping("/blogs/{blogId}")
    public BlogResponseDto updateBlog(@PathVariable Long blogId, @RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new BlogResponseDto(blogService.updateBlog(blogId, requestDto,userDetails.getUser()));
    }

    @DeleteMapping("/blogs/{blogId}")
    public String deleteBlog(@PathVariable Long blogId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        blogService.deleteBlog(blogId,userDetails.getUser());
        return "삭제 완료되었습니다.";

    }
//reply------------------------------------------------------------------------------------------------

    @PostMapping("/blogs/{blogId}/replies")
    public ReplyResponseDto addReply(@RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long blogId) {
        return new ReplyResponseDto(blogService.addReply(requestDto,userDetails.getUser(),blogId));
    }

    @GetMapping("/blogs/{blogId}/replies")
    public List<ReplyResponseDto> getReplies(@PathVariable Long blogId) {
        return blogService.getReplies(blogId);
    }


    @PutMapping("/blogs/{blogId}/replies/{replyId}")
    public ReplyResponseDto updateBlog(@PathVariable Long blogId, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long replyId) {
        return new ReplyResponseDto(blogService.updateReply(blogId, requestDto,userDetails.getUser(),replyId));
    }

    @DeleteMapping("/blogs/{blogId}/replies/{replyId}")
    public String deleteReply(@PathVariable Long blogId,@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long replyId) {
        return blogService.deleteReply(blogId,userDetails.getUser(),replyId);
    }
}
