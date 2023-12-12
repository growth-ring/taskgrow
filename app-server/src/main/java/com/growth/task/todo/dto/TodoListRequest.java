package com.growth.task.todo.dto;

import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoListRequest {
    private Status status;

    @Builder
    public TodoListRequest(Status status) {
        this.status = status;
    }
}
