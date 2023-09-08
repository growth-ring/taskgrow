package com.growth.task.todo.dto.composite;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.dto.response.PomodoroUpdateResponse;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TodoAndPomodoroUpdateResponse {

    private TodoUpdateResponse todoUpdateResponse;
    private PomodoroUpdateResponse pomodoroUpdateResponse;

    @Builder
    public TodoAndPomodoroUpdateResponse(TodoUpdateResponse todoUpdateResponse, PomodoroUpdateResponse pomodoroUpdateResponse) {
        this.todoUpdateResponse = todoUpdateResponse;
        this.pomodoroUpdateResponse = pomodoroUpdateResponse;
    }
}
