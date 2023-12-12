package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class ReviewDetailWithTaskDateResponse {
    private Long reviewId;
    private String subject;
    private Integer feelingsScore;
    private LocalDate taskDate;
}
