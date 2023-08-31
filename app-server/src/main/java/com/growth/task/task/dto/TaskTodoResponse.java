package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskTodoResponse {
    private Long remain;
    private Long done;

    public TaskTodoResponse(Long remain, Long done) {
        this.remain = remain;
        this.done = done;
    }
}
