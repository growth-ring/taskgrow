package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewUpdateService {
    private final ReviewDetailService reviewDetailService;

    public ReviewUpdateService(ReviewDetailService reviewDetailService) {
        this.reviewDetailService = reviewDetailService;
    }

    @Transactional
    public Review update(Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewDetailService.findReviewById(reviewId);

        review.update(request);
        return review;
    }
}
