package com.growth.task.user.dto;

import com.growth.task.user.domain.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSignUpResponse {
    private Long userId;
    private String name;
    private LocalDateTime createdAt;

    public UserSignUpResponse(Long userId, String name, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
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
                user.getCreatedAt()
        );
    }
}
