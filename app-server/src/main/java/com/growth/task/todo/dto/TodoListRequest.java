package com.growth.task.todo.dto;

import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TodoListRequest {
    private Status status;
    @Builder
    public TodoListRequest(String status) {
        this.status = Status.valueOf(status);
    }
}
