package com.sparta.blog.reply.controller;

import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.common.security.UserDetailsImpl;
import com.sparta.blog.reply.dto.RepliesResponseDto;
import com.sparta.blog.reply.dto.ReplyRequestDto;
import com.sparta.blog.reply.dto.ReplyResponseDto;
import com.sparta.blog.reply.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;
    HttpServletResponse response;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/blogs/{blogId}/replies")
    public ResponseEntity<ReplyResponseDto> addReply(@RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long blogId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.addReply(requestDto, userDetails.getUser(), blogId));
    }

    @GetMapping("/blogs/{blogId}/replies")
    public ResponseEntity<RepliesResponseDto> getReplies(@PathVariable Long blogId) {
        return ResponseEntity.status(HttpStatus.OK).body(new RepliesResponseDto(replyService.getReplies(blogId)));
    }


    @PutMapping("/blogs/{blogId}/replies/{replyId}")
    public ResponseEntity<ReplyResponseDto> updateBlog(@PathVariable Long blogId, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long replyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.updateReply(blogId, requestDto, userDetails.getUser(), replyId));
    }

    @DeleteMapping("/blogs/{blogId}/replies/{replyId}")
    public ResponseEntity<ApiResponseDto> deleteReply(@PathVariable Long blogId, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long replyId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.deleteReply(blogId, userDetails.getUser(), replyId));
    }

    @RequestMapping(value = "/blogs/{blogId}/replies/{replyId}/like",
            method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<ApiResponseDto> likeEvent(@PathVariable Long replyId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.likeEvent(replyId, userDetails.getUser(), request.getMethod()));
    }


}
