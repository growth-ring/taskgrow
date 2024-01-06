package com.growth.task.review.dto;

import com.growth.task.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewDetailResponse {
    private Long reviewId;
    private String subject;
    private String contents;
    private Integer feelingsScore;

    public ReviewDetailResponse(
            Long reviewId,
            String subject,
            String contents,
            Integer feelingsScore
    ) {
        this.reviewId = reviewId;
        this.subject = subject;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }

    public ReviewDetailResponse(Review review) {
        this.reviewId = review.getId();
        this.subject = review.getSubject();
        this.contents = review.getContents();
        this.feelingsScore = review.getFeelingsScore();
    }
}
