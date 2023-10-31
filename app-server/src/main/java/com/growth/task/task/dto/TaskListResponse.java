package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Task 리스트 조회 DTO
 */
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskListResponse {
    private Long taskId;
    private Long userId;
    private LocalDate taskDate;
    private TaskTodoResponse todos;
    private Integer feelingsScore;

    public TaskListResponse(Long taskId, Long userId, LocalDate taskDate, TaskTodoResponse todos, Integer feelingsScore) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskDate = taskDate;
        this.todos = todos;
        this.feelingsScore = feelingsScore;
    }

    public TaskListResponse(TaskListQueryResponse task, TaskTodoResponse todos) {
        this.taskId = task.getTaskId();
        this.userId = task.getUserId();
        this.taskDate = task.getTaskDate();
        this.todos = todos;
        this.feelingsScore = task.getFeelingsScore();
    }
}
