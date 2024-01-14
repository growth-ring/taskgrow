package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.type.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserGetResponse {
    private Long userId;
    private String name;

    private Role role;

    public UserGetResponse(
            Long userId,
                           String name,
                           Role role
    ) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public static UserGetResponse of(Users user) {
        return new UserGetResponse(user.getUserId(), user.getName(), user.getRole());
    }
}
