package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.task.domain.Tasks;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Task 생성 응답
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskAddResponse {
    private Long taskId;
    private Long userId;
    private LocalDateTime taskDate;
    private LocalDateTime createdAt;

    @Builder
    public TaskAddResponse(Long taskId, Long userId, LocalDateTime taskDate, LocalDateTime createdAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskDate = taskDate;
        this.createdAt = createdAt;
    }

    public TaskAddResponse(Tasks task) {
        this.taskId = task.getTaskId();
        this.userId = task.getUser().getUserId();
        this.taskDate = task.getTaskDate();
        this.createdAt = task.getCreatedAt();
    }
}
