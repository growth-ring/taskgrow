package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.task.domain.Tasks;
import com.growth.task.user.domain.Users;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Task 생성 요청
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskAddRequest {
    @NotNull(message = "user는 필수 입력 값입니다.")
    private Long userId;
    @NotNull(message = "테스크 날짜는 필수 입력 값입니다.")
    private LocalDateTime taskDate;

    @Builder
    public TaskAddRequest(Long userId, LocalDateTime taskDate) {
        this.userId = userId;
        this.taskDate = taskDate;
    }

    public Tasks toEntity(Users user) {
        return Tasks.builder()
                .user(user)
                .taskDate(taskDate)
                .build();
    }
}
