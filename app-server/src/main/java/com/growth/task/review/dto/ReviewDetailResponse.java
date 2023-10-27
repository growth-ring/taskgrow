package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.review.domain.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewDetailResponse {
    private Long reviewId;
    private String contents;
    private Integer feelingsScore;

    public ReviewDetailResponse(Long reviewId, String contents, Integer feelingsScore) {
        this.reviewId = reviewId;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }

    public ReviewDetailResponse(Review review) {
        this.reviewId = review.getId();
        this.contents = review.getContents();
        this.feelingsScore = review.getFeelingsScore();
    }
}
