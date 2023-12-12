package com.growth.task.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.enums.Status;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
            LocalDate start_date,
            LocalDate end_date
    ) {
        this.status = status;
        this.endDate = end_date;
        this.startDate = start_date;
    }
}
