package com.sparta.blog.reply.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RepliesResponseDto {
    private List<ReplyResponseDto> replyList;

    public RepliesResponseDto(List<ReplyResponseDto> replyList) {
        this.replyList = replyList;
    }
}

