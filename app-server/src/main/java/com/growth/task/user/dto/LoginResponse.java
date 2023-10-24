package com.growth.task.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponse {
    private String name;

    public LoginResponse(String name) {
        this.name = name;
    }
}
