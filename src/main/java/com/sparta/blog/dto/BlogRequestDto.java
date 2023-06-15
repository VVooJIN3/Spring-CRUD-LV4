package com.sparta.blog.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class BlogRequestDto {
    private String title;
    private String username;
    private String contents;
    private LocalDateTime dateTime;

}
