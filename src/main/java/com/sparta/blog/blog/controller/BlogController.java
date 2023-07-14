package com.sparta.blog.blog.controller;

import com.sparta.blog.blog.dto.BlogRequestDto;
import com.sparta.blog.blog.dto.BlogResponseDto;
import com.sparta.blog.blog.dto.BlogsResponseDto;
import com.sparta.blog.blog.service.BlogService;
import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.common.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;
    HttpServletResponse response;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    //예외처리 메서드
    //컨트롤러 내 API가 호출되다가 Exception 발생 시, 코드 실행
    @ExceptionHandler
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(ex.getMessage()));
    }

    //게시글 작성
    @PostMapping("/blogs")
    public ResponseEntity<BlogResponseDto> createBlog(@RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
            return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(blogService.createBlog(requestDto, userDetails.getUser()));
    }

    //게시글 조회(전체)
    @GetMapping("/blogs")
    public ResponseEntity<BlogsResponseDto> getBlogs() {
        return ResponseEntity.status(HttpStatus.OK).body(new BlogsResponseDto(blogService.getBlogs()));
    }

    //게시글 조회(선택 id)
    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<BlogResponseDto> getBlog(@PathVariable Long blogId) {
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlog(blogId));
    }

    //게시글 수정(선택 id)
    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<BlogResponseDto> updateBlog(@PathVariable Long blogId, @RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.updateBlog(blogId, requestDto,userDetails.getUser()));
    }

    //게시글 삭제(선택 id)
    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<ApiResponseDto> deleteBlog(@PathVariable Long blogId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(blogService.deleteBlog(blogId,userDetails.getUser()));
    }

    //좋아요 클릭 이벤트(증가 또는 감소)
    @RequestMapping(value = "/blogs/{blogId}/like",
                    method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<ApiResponseDto> likeEvent(@PathVariable Long blogId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.likeEvent(blogId, userDetails.getUser(),request.getMethod()));
    }

}
