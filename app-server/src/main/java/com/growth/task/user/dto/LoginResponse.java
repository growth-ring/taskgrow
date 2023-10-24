package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponse {
    private Long id;
    private String name;

    public LoginResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LoginResponse(Users users) {
        this.id = users.getUserId();
        this.name = users.getName();
    }
}
