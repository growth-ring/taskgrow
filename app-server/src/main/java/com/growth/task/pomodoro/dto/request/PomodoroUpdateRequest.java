package com.growth.task.pomodoro.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.todo.domain.Todos;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PomodoroUpdateRequest {
    private int planCount;

    @Builder
    public PomodoroUpdateRequest(int planCount) {
        this.planCount = planCount;
    }

    public Pomodoros toEntity(Todos todos) {
        return Pomodoros.builder()
                .todo(todos)
                .planCount(this.planCount)
                .build();
    }
}
