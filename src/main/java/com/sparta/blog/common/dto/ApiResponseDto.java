package com.sparta.blog.common.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDto {
    private String message;

    public ApiResponseDto(String message) {
        this.message = message;
    }
}
