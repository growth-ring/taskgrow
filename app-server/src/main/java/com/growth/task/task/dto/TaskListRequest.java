package com.growth.task.task.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class TaskListRequest {
    @NotNull(message = "사용자 id는 필수입니다.")
    private Long userId;
    @NotNull(message = "검색 범위 시작 날짜를 지정해야 합니다.")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "검색 범위 끝 날짜를 지정해야 합니다.")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public TaskListRequest(Long userId, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
