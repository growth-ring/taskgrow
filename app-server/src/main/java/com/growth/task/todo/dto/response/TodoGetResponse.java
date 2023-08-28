package com.growth.task.todo.dto.response;

import com.growth.task.todo.enums.Status;
import lombok.Data;

@Data
public class TodoGetResponse {
    private Long todoId;
    private String todo;
    private Status status;

    public TodoGetResponse(Long todoId, String todo, Status status) {
        this.todoId = todoId;
        this.todo = todo;
        this.status = status;
    }
}
