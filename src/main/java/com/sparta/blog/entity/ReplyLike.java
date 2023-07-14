package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "replyLike")
@NoArgsConstructor
public class ReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ReplyLike(Reply reply,User user){
        this.user = user;
        this.reply = reply;
    }
}
