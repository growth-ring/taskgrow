package com.growth.task.review.service;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewDetailWithTaskDateResponse;
import com.growth.task.review.dto.ReviewListRequest;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.dto.ReviewStatsResponse;
import com.growth.task.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.growth.task.review.domain.Review.FEELING_SCORE_LOWER_BOUND;
import static com.growth.task.review.domain.Review.FEELING_SCORE_UPPER_BOUND;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ReviewListService {
    public static final long DEFAULT_SCORE_COUNT = 0L;
    private final ReviewRepository reviewRepository;

    public ReviewListService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * 사용자가 기록한 감정 기록 통계를 조회한다.
     *
     * @param userId  사용자 id
     * @param request 날짜 범위
     * @return 감정 기록 통계
     */
    @Transactional(readOnly = true)
    public ReviewStatsResponse getReviewStats(Long userId, ReviewStatsRequest request) {
        List<ReviewDetailResponse> reviews = getReviewList(userId, request);

        Map<Integer, Long> feelings = addDefaultScore(countFeelingsScore(reviews));

        return new ReviewStatsResponse(feelings);
    }

    /**
     * 사용자가 기록한 회고 리스트를 조회한다
     *
     * @param userId  사용자 id
     * @param request 날짜 범위
     * @return 감정 기록 리스트
     */
    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewList(Long userId, ReviewStatsRequest request) {
        return reviewRepository.findByUserIdAndBetweenTimeRange(userId, request);
    }

    /**
     * 사용자가 기록한 감정점수에 따른 회고 리스트를 조회한다
     *
     * @param pageable 페이징
     * @param userId   사용자 리스트
     * @param request  감정 점수, 날짜
     * @return 감정 기록 리스트
     */
    @Transactional(readOnly = true)
    public Page<ReviewDetailWithTaskDateResponse> getReviewByUserIdAndParams(Pageable pageable, Long userId, ReviewListRequest request) {
        return reviewRepository.findByUserAndParams(pageable, userId, request.getFeelingsScore(), request.getStartDate(), request.getEndDate());
    }

    private Map<Integer, Long> countFeelingsScore(List<ReviewDetailResponse> reviews) {
        return reviews.stream()
                .collect(groupingBy(
                        ReviewDetailResponse::getFeelingsScore,
                        counting()
                ));
    }

    private Map<Integer, Long> addDefaultScore(Map<Integer, Long> feelings) {
        IntStream.rangeClosed(FEELING_SCORE_LOWER_BOUND, FEELING_SCORE_UPPER_BOUND)
                .forEach(i -> feelings.putIfAbsent(i, DEFAULT_SCORE_COUNT));
        return feelings;
    }
}
