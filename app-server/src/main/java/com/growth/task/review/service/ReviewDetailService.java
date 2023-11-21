package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.exception.ReviewNotFoundException;
import com.growth.task.review.repository.ReviewRepository;
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
    public ReviewDetailResponse getReviewByTaskId(Long taskId) {
        Review review = findReviewByTaskId(taskId);
        return new ReviewDetailResponse(review);
    }

    @Transactional(readOnly = true)
    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }

    @Transactional(readOnly = true)
    public Review findReviewByTaskId(Long taskId) {
        return reviewRepository.findByTasks_TaskId(taskId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다."));
    }
}
