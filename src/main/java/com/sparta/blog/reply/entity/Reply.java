package com.sparta.blog.reply.entity;

import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.common.entity.Timestamped;
import com.sparta.blog.reply.dto.ReplyRequestDto;
import com.sparta.blog.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reply")
@NoArgsConstructor
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String username;

    @Column(nullable = false, unique = false, length = 500)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE)
    private List<ReplyLike> replyLikes;

    private int likeCnt;

    public Reply(ReplyRequestDto requestDto, User user, Blog blog) {
//        this.username = requestDto.getUsername();
        this.username = user.getUsername();
        this.comment = requestDto.getComment();
        this.blog = blog;
        this.likeCnt = 0;
    }

    public void update(ReplyRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }

    public void increaseLike() {
        this.likeCnt++;
    }

    public void decreaseLike() {
        this.likeCnt--;
    }
}
