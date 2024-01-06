package com.growth.task.review.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewAddResponse {
    private Long reviewId;

    public ReviewAddResponse(Long reviewId) {
        this.reviewId = reviewId;
    }
}
