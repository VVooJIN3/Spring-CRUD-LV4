package com.sparta.blog.blog.entity;

import com.sparta.blog.blog.dto.BlogRequestDto;
import com.sparta.blog.common.entity.Timestamped;
import com.sparta.blog.reply.entity.Reply;
import com.sparta.blog.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    private List<Reply> replies;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    private List<BlogLike> blogLikes;

    private int likeCnt;

    public Blog(BlogRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.username = user.getUsername();
        this.contents = requestDto.getContents();
        this.user = user;
        this.likeCnt = 0;
    }

    public void update(BlogRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void increaseLike(){
        this.likeCnt++;
    }
    public void decreaseLike(){
        this.likeCnt--;
    }


}

