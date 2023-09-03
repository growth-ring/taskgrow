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
    private TodoAddResponse todoAddResponse;
    private PomodoroAddResponse pomodoroAddResponse;

    @Builder
    public TodoAndPomodoroAddResponse(TodoAddResponse todoAddResponse, PomodoroAddResponse pomodoroAddResponse) {
        this.todoAddResponse = todoAddResponse;
        this.pomodoroAddResponse = pomodoroAddResponse;
    }
}
