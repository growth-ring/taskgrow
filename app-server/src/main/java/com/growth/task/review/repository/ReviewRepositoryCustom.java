package com.growth.task.review.repository;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewStatsRequest;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewDetailResponse> findByUserIdAndBetweenTimeRange(Long userId, ReviewStatsRequest request);
}
