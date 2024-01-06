package com.growth.task.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;


@Getter
public class ReviewListRequest {
    private static final String FEELINGS_SCORE_VALID_MESSAGE = "기분 점수는 1 ~ 10 사이를 입력해주세요.";
    private List<Integer> feelingsScore;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endDate;

    @Builder
    public ReviewListRequest(
            List<Integer> feelingsScore,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.feelingsScore = feelingsScore;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
