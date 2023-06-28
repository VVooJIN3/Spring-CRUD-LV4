package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BlogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUsername();
        this.contents = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
    }

//    public BlogResponseDto(Long id, String title, String username, String contents, LocalDateTime dateTime) {
//        this.id = id;
//        this.title = title;
//        this.username = username;
//        this.contents = contents;
//        this.dateTime = dateTime;
//    }
}
