package com.growth.task.todo.dto.response;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;

/**
 * Todo 생성 응답
 */
@Getter
public class TodoAddResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private Status status;
    private int orderNo;

    @Builder
    public TodoAddResponse(Long todoId, Long taskId, String todo, Status status, int orderNo) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
    }

    public TodoAddResponse(Todos todos) {
        this.todoId = todos.getTodoId();
        this.taskId = todos.getTask().getTaskId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
        this.orderNo = todos.getOrderNo();
    }
}
