package com.growth.task.todo.dto.response;

import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class TodoDetailResponse {
    private String todo;
    private Status status;
    private int performCount;
    private int planCount;
    private LocalDate taskDate;

    @Builder
    public TodoDetailResponse(
            String todo,
            Status status,
            int performCount,
            int planCount,
            LocalDate taskDate
    ) {
        this.todo = todo;
        this.status = status;
        this.performCount = performCount;
        this.planCount = planCount;
        this.taskDate = taskDate;
    }
}
