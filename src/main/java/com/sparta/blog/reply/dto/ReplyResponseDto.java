package com.sparta.blog.reply.dto;

import com.sparta.blog.reply.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private Long blogId;
    private String username;
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCnt;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.blogId = reply.getBlog().getId();
        this.username = reply.getUsername();
        this.comment = reply.getComment();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
        this.likeCnt = reply.getLikeCnt();
    }
}

