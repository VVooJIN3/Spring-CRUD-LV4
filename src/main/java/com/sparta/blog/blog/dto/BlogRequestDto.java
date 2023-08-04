package com.sparta.blog.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogRequestDto {
    private String title;
    //    private String username;
    private String contents;
//    private LocalDateTime dateTime;
//    private LocalDateTime updateDateTime;

    @Builder
    public BlogRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
