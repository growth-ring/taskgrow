package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ReviewListRequest {
    private static final String FEELINGS_SCORE_VALID_MESSAGE = "기분 점수는 1 ~ 10 사이를 입력해주세요.";

    @Min(value = 1, message = FEELINGS_SCORE_VALID_MESSAGE)
    @Max(value = 10, message = FEELINGS_SCORE_VALID_MESSAGE)
    private Integer feelingsScore;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public ReviewListRequest(
            Integer feelings_score,
            LocalDate start_date,
            LocalDate end_date
    ) {
        this.feelingsScore = feelings_score;
        this.startDate = start_date;
        this.endDate = end_date;
    }
}
