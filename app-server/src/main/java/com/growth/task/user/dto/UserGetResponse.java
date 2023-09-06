package com.growth.task.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
