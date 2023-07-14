package com.sparta.blog.blog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BlogsResponseDto {
    private List<BlogResponseDto> blogsList;

    public BlogsResponseDto(List<BlogResponseDto> blogsList) {
        this.blogsList = blogsList;
    }
}
