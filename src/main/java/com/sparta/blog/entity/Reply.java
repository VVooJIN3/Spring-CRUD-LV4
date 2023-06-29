package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reply")
@NoArgsConstructor
public class Reply  extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String username;

    @Column(nullable = false, unique = false,length = 500)
    private String comment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    public Reply (ReplyRequestDto requestDto, User user, Blog blog) {
//        this.username = requestDto.getUsername();
        this.username = user.getUsername();
        this.comment = requestDto.getComment();
        this.blog= blog;

    }
    public void update(ReplyRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
