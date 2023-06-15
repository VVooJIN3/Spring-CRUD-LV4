package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Blog {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime dateTime;

    public Blog(Long id, String title, String username, String contents, LocalDateTime dateTime)
    {
    this.title = title;
    this.username = username;
    this.contents = contents;
    this.dateTime = dateTime;
    }
    public LocalDateTime getDateTime(){
        return LocalDateTime.now();
    }
    public Blog(BlogRequestDto requestDto){

        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.dateTime= requestDto.getDateTime();
    }

}

