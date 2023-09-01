package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Task의 Todos 진행률 DTO
 */
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskTodoResponse {
    private int remain;
    private int done;

    public TaskTodoResponse(int remain, int done) {
        this.remain = remain;
        this.done = done;
    }
}
