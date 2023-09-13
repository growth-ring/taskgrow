package com.growth.task.todo.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;

/**
 * Todo 업데이튼 응답
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoUpdateResponse {
    private String todo;
    private Status status;

    @Builder
    public TodoUpdateResponse(String todo, Status status) {
        this.todo = todo;
        this.status = status;
    }

    public TodoUpdateResponse(Todos todos) {
        this.todo = todos.getTodo();
        this.status = todos.updateStatus();
    }
}
