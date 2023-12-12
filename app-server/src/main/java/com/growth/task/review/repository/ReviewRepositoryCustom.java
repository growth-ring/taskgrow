package com.growth.task.review.repository;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewDetailWithTaskDateResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewDetailResponse> findByUserIdAndBetweenTimeRange(Long userId, ReviewStatsRequest request);

    Page<ReviewDetailWithTaskDateResponse> findByUserAndParams(Pageable pageable, Long userId, Integer feelingsScore, LocalDate startDate, LocalDate endDate);
}
