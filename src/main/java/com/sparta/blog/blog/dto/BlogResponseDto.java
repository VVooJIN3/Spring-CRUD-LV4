package com.sparta.blog.blog.dto;

import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.reply.dto.ReplyResponseDto;
import com.sparta.blog.reply.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BlogResponseDto{
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int LikeCnt;
    private List<ReplyResponseDto> replies = new ArrayList<>();
    public BlogResponseDto(Blog blog){
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUsername();
        this.contents = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.LikeCnt = blog.getLikeCnt();
        if(blog.getReplies()!=null) {
            for (Reply reply : blog.getReplies()) {
                replies.add(new ReplyResponseDto(reply));
            }
        }
    }

}
