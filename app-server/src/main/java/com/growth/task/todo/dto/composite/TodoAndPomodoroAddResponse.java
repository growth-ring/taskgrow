package com.growth.task.todo.dto.composite;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.dto.response.PomodoroAddResponse;
import com.growth.task.todo.dto.response.TodoAddResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoAndPomodoroAddResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private String status;
    private int performCount;
    private int planCount;

    @Builder
    public TodoAndPomodoroAddResponse(Long todoId, Long taskId, String todo, String status, int performCount, int planCount) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public TodoAndPomodoroAddResponse(TodoAddResponse todoAddResponse, PomodoroAddResponse pomodoroAddResponse) {
        this.todoId = todoAddResponse.getTodoId();
        this.taskId = todoAddResponse.getTaskId();
        this.todo = todoAddResponse.getTodo();
        this.status = todoAddResponse.getStatus().toString(); // Enum을 문자열로 변환
        this.performCount = pomodoroAddResponse.getPerformCount();
        this.planCount = pomodoroAddResponse.getPlanCount();
    }

    public static TodoAndPomodoroAddResponse from(TodoAddResponse todoResponse, PomodoroAddResponse pomodoroResponse) {
        return TodoAndPomodoroAddResponse.builder()
                .todoId(todoResponse.getTodoId())
                .taskId(todoResponse.getTaskId())
                .todo(todoResponse.getTodo())
                .status(todoResponse.getStatus().toString())
                .performCount(pomodoroResponse.getPerformCount())
                .planCount(pomodoroResponse.getPlanCount())
                .build();
    }
}
