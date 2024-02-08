package com.growth.task.todo.dto.response;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;

/**
 * 투두 업데이트 응답
 */
@Getter
public class TodoUpdateResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private Status status;
    private int orderNo;

    @Builder
    public TodoUpdateResponse(Long todoId, Long taskId, String todo, Status status, int orderNo) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
        this.orderNo = orderNo;
    }

    public TodoUpdateResponse(Todos todos) {
        this.todoId = todos.getTodoId();
        this.taskId = todos.getTask().getTaskId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
        this.orderNo = todos.getOrderNo();
    }
}
