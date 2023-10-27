package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.exception.ReviewNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewDetailService {
    private final ReviewRepository reviewRepository;

    public ReviewDetailService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public ReviewDetailResponse getReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        return new ReviewDetailResponse(review);
    }

    @Transactional(readOnly = true)
    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}
