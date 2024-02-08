package com.growth.task.todo.dto.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.todo.dto.request.TodoAddRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoAndPomodoroAddRequest {
    @Valid
    @JsonIgnore
    private TodoAddRequest todoAddRequest;

    @Valid
    @JsonIgnore
    private PomodoroAddRequest pomodoroAddRequest;

    @JsonCreator
    public TodoAndPomodoroAddRequest(
            @JsonProperty("taskId") Long taskId,
            @JsonProperty("todo") String todo,
            @JsonProperty("orderNo") int orderNo,
            @JsonProperty("performCount") int performCount,
            @JsonProperty("planCount") int planCount
    ) {
        this.todoAddRequest = new TodoAddRequest(taskId, todo, orderNo);
        this.pomodoroAddRequest = new PomodoroAddRequest(performCount, planCount);
    }

    @Builder
    public TodoAndPomodoroAddRequest(TodoAddRequest todoAddRequest, PomodoroAddRequest pomodoroAddRequest) {
        this.todoAddRequest = todoAddRequest;
        this.pomodoroAddRequest = pomodoroAddRequest;
    }

    public Long getTaskId() {
        return todoAddRequest.getTaskId();
    }

    public String getTodo() {
        return todoAddRequest.getTodo();
    }

    public int getOrderNo() {
        return todoAddRequest.getOrderNo();
    }

    public int getPerformCount() {
        return pomodoroAddRequest.getPerformCount();
    }

    public int getPlanCount() {
        return pomodoroAddRequest.getPlanCount();
    }
}
