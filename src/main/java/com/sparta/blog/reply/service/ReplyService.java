package com.sparta.blog.reply.service;

import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.blog.repository.BlogRepository;
import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.reply.dto.ReplyRequestDto;
import com.sparta.blog.reply.dto.ReplyResponseDto;
import com.sparta.blog.reply.entity.Reply;
import com.sparta.blog.reply.entity.ReplyLike;
import com.sparta.blog.reply.repository.ReplyLikeRepository;
import com.sparta.blog.reply.repository.ReplyRepository;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ReplyService {

    List<ReplyResponseDto> getReplies(Long blogId) ;

    @Transactional
    ReplyResponseDto addReply(ReplyRequestDto requestDto, User user, Long blogId);
    @Transactional
    ReplyResponseDto updateReply(Long blogId, ReplyRequestDto requestDto, User user, Long replyId);


    @Transactional
    ApiResponseDto deleteReply(Long blogId, User user, Long replyId);

    @Transactional
    public ApiResponseDto likeEvent(Long id, User user, String requestMethod);

    @Transactional
    public void increaseLike(Reply reply, User user) ;

    @Transactional
    public void decreaseLike(Reply reply, ReplyLike replyLike) ;




}
