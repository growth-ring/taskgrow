package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class TaskTodoDetailResponse {
    private String todo;
    private Integer performCount;
    private Integer planCount;

    public TaskTodoDetailResponse(String todo, int performCount, int planCount) {
        this.todo = todo;
        this.performCount = performCount;
        this.planCount = planCount;
    }
}
