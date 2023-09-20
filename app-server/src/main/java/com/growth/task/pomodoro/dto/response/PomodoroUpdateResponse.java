package com.growth.task.pomodoro.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.pomodoro.domain.Pomodoros;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PomodoroUpdateResponse {
    @JsonProperty("perform_count")
    private int performCount;
    @JsonProperty("plan_count")
    private int planCount;

    @Builder
    public PomodoroUpdateResponse(int performCount, int planCount) {
        this.performCount = performCount;
        this.planCount = planCount;
    }

    public PomodoroUpdateResponse(Pomodoros pomodoros) {
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
    }
}
