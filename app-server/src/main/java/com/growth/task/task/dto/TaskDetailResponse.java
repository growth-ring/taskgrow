package com.growth.task.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


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
