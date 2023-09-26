package com.growth.task.pomodoro.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.domain.Pomodoros;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PomodoroCompleteResponse {
    private int performCount;
    private int planCount;

    @Builder
    public PomodoroCompleteResponse(int performCount, int planCount) {
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public PomodoroCompleteResponse(Pomodoros pomodoros) {
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
    }
}
