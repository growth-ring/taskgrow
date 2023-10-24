package com.growth.task.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginRequest {
    @NotBlank(message = "이름을 필수입니다.")
    private String name;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
