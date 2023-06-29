package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.entity.Timestamped;
import com.sparta.blog.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "blog")
@NoArgsConstructor
public class Blog extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    public Blog(BlogRequestDto requestDto){
//        this.title = requestDto.getTitle();
//        this.username = requestDto.getUsername();
//        this.contents = requestDto.getContents();
//    }

    public Blog(BlogRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
//        this.username = requestDto.getUsername();
        this.username = user.getUsername();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(BlogRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}

