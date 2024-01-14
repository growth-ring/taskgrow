package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.growth.task.user.domain.Users.ofUser;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSignUpRequest {
    @NotNull(message = "이름은 필수값입니다.")
    private String name;
    @NotNull(message = "비밀번호는 필수값입니다.")
    private String password;

    @Builder
    public UserSignUpRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Users toEntity() {
        return ofUser(name, password);
    }
}
