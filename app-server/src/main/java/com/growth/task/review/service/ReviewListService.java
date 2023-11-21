package com.growth.task.review.service;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.dto.ReviewStatsResponse;
import com.growth.task.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Transactional
@Service
public class ReviewListService {
    private final ReviewRepository reviewRepository;

    public ReviewListService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public ReviewStatsResponse getReviewStats(Long userId, ReviewStatsRequest request) {
        List<ReviewDetailResponse> reviews = getReviewList(userId, request);
        Map<Integer, Long> feelings = reviews.stream()
                .collect(groupingBy(ReviewDetailResponse::getFeelingsScore, counting()));

        return new ReviewStatsResponse(feelings);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewList(Long userId, ReviewStatsRequest request) {
        return reviewRepository.findByUserIdAndBetweenTimeRange(userId, request);
    }
}
