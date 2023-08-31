package com.growth.task.todo.dto.response;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Data;

@Data
public class TodoGetResponse {
    private Long todoId;
    private String todo;
    private Status status;
    private Integer performCount;
    private Integer planCount;

    public TodoGetResponse(Long todoId, String todo, Status status, Integer performCount, Integer planCount) {
        this.todoId = todoId;
        this.todo = todo;
        this.status = status;
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public TodoGetResponse(Todos todos, Pomodoros pomodoros) {
        this.todoId = todos.getTodoId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
    }
}
