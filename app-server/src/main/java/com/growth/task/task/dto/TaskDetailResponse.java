package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class TaskDetailResponse {
    private Long taskId;
    private List<TaskTodoDetailResponse> todos;

    public TaskDetailResponse(Long taskId, List<TaskTodoDetailResponse> todos) {
        this.taskId = taskId;
        this.todos = todos;
    }
}
