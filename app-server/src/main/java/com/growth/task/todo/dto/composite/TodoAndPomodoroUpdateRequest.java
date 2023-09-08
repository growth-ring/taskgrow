package com.growth.task.todo.dto.composite;

import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoAndPomodoroUpdateRequest {
    @Valid
    private TodoUpdateRequest todoUpdateRequest;

    @Valid
    private PomodoroUpdateRequest pomodoroUpdateRequest;

    @Builder
    public TodoAndPomodoroUpdateRequest(TodoUpdateRequest todoUpdateRequest, PomodoroUpdateRequest pomodoroUpdateRequest) {
        this.todoUpdateRequest = todoUpdateRequest;
        this.pomodoroUpdateRequest = pomodoroUpdateRequest;
    }
}
