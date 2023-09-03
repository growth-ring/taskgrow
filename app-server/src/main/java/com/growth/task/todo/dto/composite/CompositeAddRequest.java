package com.growth.task.todo.dto.composite;

import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.todo.dto.request.TodoAddRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CompositeAddRequest {
    @Valid
    private TodoAddRequest todoAddRequest;

    @Valid
    private PomodoroAddRequest pomodoroAddRequest;

    @Builder
    public CompositeAddRequest(TodoAddRequest todoAddRequest, PomodoroAddRequest pomodoroAddRequest) {
        this.todoAddRequest = todoAddRequest;
        this.pomodoroAddRequest = pomodoroAddRequest;
    }
}