package com.sparta.blog.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchCond {
    private Long username;
    private Long mentionedUserId; // 멘션된 유저 ID
}
