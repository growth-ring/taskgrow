package com.growth.task.todo.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TodoStatusUpdateResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private Status status;

    @Builder
    public TodoStatusUpdateResponse(Long todoId, Long taskId, String todo, Status status) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
    }

    public TodoStatusUpdateResponse(Long todoId, Todos todos) {
        this.todoId = todoId;
        this.taskId = todos.getTask().getTaskId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
    }
}
