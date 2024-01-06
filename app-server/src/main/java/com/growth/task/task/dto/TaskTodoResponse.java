package com.growth.task.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Task의 Todos 진행률 DTO
 */
@NoArgsConstructor
@Getter
public class TaskTodoResponse {
    private int remain;
    private int done;

    public TaskTodoResponse(int remain, int done) {
        this.remain = remain;
        this.done = done;
    }
}
