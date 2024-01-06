package com.growth.task.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
