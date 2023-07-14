package com.sparta.blog.controller;

import com.sparta.blog.dto.*;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.BlogService;
import com.sparta.blog.service.ReplyService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;
    HttpServletResponse response;
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }


    //예외처리 메서드
    //컨트롤러 내 API가 호출되다가 Exception 발생 시, 코드 실행
    @ExceptionHandler
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(ex.getMessage()));
    }

    @PostMapping("/blogs/{blogId}/replies")
    public ResponseEntity<ReplyResponseDto> addReply(@RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long blogId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.addReply(requestDto,userDetails.getUser(),blogId));
    }

    @GetMapping("/blogs/{blogId}/replies")
    public ResponseEntity<RepliesResponseDto> getReplies(@PathVariable Long blogId) {
        return ResponseEntity.status(HttpStatus.OK).body(new RepliesResponseDto(replyService.getReplies(blogId)));
    }


    @PutMapping("/blogs/{blogId}/replies/{replyId}")
    public ResponseEntity<ReplyResponseDto> updateBlog(@PathVariable Long blogId, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long replyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.updateReply(blogId, requestDto,userDetails.getUser(),replyId));
    }

    @DeleteMapping("/blogs/{blogId}/replies/{replyId}")
    public ResponseEntity<ApiResponseDto> deleteReply(@PathVariable Long blogId,@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long replyId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.deleteReply(blogId,userDetails.getUser(),replyId));
    }

    @RequestMapping(value ="/blogs/{blogId}/replies/{replyId}/like",
                    method = {RequestMethod.POST,RequestMethod.DELETE})
    public ResponseEntity<ApiResponseDto> likeEvent(@PathVariable Long replyId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(replyService.likeEvent(replyId, userDetails.getUser(),request.getMethod()));
    }


}
