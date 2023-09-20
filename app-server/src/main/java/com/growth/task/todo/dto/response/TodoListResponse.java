package com.growth.task.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Getter;

@Getter
public class TodoListResponse {
    @JsonProperty("todo_id")
    private Long todoId;
    @JsonProperty("task_id")
    private Long taskId;
    private String todo;
    private Status status;
    @JsonProperty("perform_count")
    private Integer performCount;
    @JsonProperty("plan_count")
    private Integer planCount;

    public TodoListResponse(Long todoId, Long taskId, String todo, Status status, Integer performCount, Integer planCount) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public TodoListResponse(Todos todos, Pomodoros pomodoros) {
        this.todoId = todos.getTodoId();
        this.taskId = todos.getTask().getTaskId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
    }
}
