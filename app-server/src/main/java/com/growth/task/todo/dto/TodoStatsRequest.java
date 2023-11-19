package com.growth.task.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TodoStatsRequest {
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public TodoStatsRequest(LocalDate start_date, LocalDate end_date) {
        this.startDate = start_date;
        this.endDate = end_date;
    }
}
