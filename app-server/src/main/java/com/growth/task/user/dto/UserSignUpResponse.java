package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.type.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSignUpResponse {
    private Long userId;
    private String name;

    private Role role;
    private LocalDateTime createdAt;

    public UserSignUpResponse(
            Long userId,
            String name,
            Role role,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    /**
     * User Entity -> UserSignUpResponse DTO
     *
     * @param user
     * @return
     */
    public static UserSignUpResponse of(Users user) {
        return new UserSignUpResponse(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
