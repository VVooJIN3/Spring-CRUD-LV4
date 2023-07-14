package com.sparta.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequestDto {
    private String title;
//    private String username;
    private String contents;
//    private LocalDateTime dateTime;
//    private LocalDateTime updateDateTime;
}
