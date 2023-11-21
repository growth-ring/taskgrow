package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.exception.ReviewNotFoundException;
import com.growth.task.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewDeleteService {
    private final ReviewRepository reviewRepository;

    public ReviewDeleteService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void deleteById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        reviewRepository.delete(review);
    }
}
