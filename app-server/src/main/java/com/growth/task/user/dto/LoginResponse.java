package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.type.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginResponse {
    private Long userId;
    private String name;

    private Role role;

    public LoginResponse(Users users) {
        this.userId = users.getUserId();
        this.name = users.getName();
        this.role = users.getRole();
    }
}
