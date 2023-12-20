package com.growth.task.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class TodoResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private Status status;

    public TodoResponse(Long todoId, Long taskId, String todo, Status status) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
    }
}
