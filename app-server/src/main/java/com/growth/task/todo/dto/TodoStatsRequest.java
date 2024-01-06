package com.growth.task.todo.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class TodoStatsRequest {
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public TodoStatsRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
