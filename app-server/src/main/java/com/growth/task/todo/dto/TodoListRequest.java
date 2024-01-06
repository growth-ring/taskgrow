package com.growth.task.todo.dto;

import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class TodoListRequest {
    private Status status;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public TodoListRequest(
            Status status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.status = status;
        this.endDate = endDate;
        this.startDate = startDate;
    }
}
