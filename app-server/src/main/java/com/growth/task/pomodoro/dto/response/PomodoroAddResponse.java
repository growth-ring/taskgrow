package com.growth.task.pomodoro.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.domain.Pomodoros;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PomodoroAddResponse {
    private int performCount;
    private int planCount;

    @Builder
    public PomodoroAddResponse(int performCount, int planCount) {
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public PomodoroAddResponse(Pomodoros pomodoros) {
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
    }
}