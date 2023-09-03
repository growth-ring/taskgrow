package com.growth.task.pomodoro.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.todo.domain.Todos;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PomodoroAddRequest {

    private int performCount;
    @NotNull(message = "계획 횟수는 필수 입력 값입니다.")
    private int planCount;


    @Builder
    public PomodoroAddRequest(int performCount, int planCount) {
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public Pomodoros toEntity(Todos todos) {
        return Pomodoros.builder()
                .todo(todos)
                .performCount(this.performCount)
                .planCount(this.planCount)
                .build();
    }
}
