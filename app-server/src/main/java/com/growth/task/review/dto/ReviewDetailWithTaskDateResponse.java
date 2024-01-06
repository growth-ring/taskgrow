package com.growth.task.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ReviewDetailWithTaskDateResponse {
    private Long reviewId;
    private String subject;
    private Integer feelingsScore;
    private LocalDate taskDate;
}
