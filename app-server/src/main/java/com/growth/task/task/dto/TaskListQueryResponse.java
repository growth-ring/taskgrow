package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Task 리스트 쿼리 조회 응답 DTO
 */
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskListQueryResponse {
    private Long taskId;
    private Long userId;
    private LocalDate taskDate;
    private Status todoStatus;
    private Integer feelingsScore;

    public TaskListQueryResponse(Long taskId, Long userId, LocalDate taskDate, Status todoStatus, Integer feelingsScore) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskDate = taskDate;
        this.todoStatus = todoStatus;
        this.feelingsScore = feelingsScore;
    }
}
