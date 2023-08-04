package com.sparta.blog.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
// 아무런 값도 가지지 않는 의미 없는 객체의 생성을 막음
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // enum의 이름 그대로를 저장 USER -> USER
    private UserRoleEnum role;
    
    @Builder
    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
