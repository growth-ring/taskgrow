package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserGetResponse {
    private Long userId;
    private String name;

    public UserGetResponse(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static UserGetResponse of(Users user) {
        return new UserGetResponse(user.getUserId(), user.getName());
    }
}
