package com.growth.task.review.service;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.dto.ReviewStatsResponse;
import com.growth.task.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewListServiceTest {
    public static final long USER_ID = 1L;
    public static final LocalDate LOCAL_DATE_11_20 = LocalDate.of(2023, 11, 20);
    public static final LocalDate LOCAL_DATE_11_19 = LocalDate.of(2023, 11, 19);
    private ReviewListService reviewListService;
    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewListService = new ReviewListService(reviewRepository);
    }

    @Nested
    @DisplayName("getReviewStats")
    class Describe_getReviewStats {
        @Nested
        @DisplayName("사용자 아이다와 날짜가 주어지면")
        class Context_with_user_id_and_time_range {
            private ReviewStatsRequest request = new ReviewStatsRequest(LOCAL_DATE_11_19, LOCAL_DATE_11_20);

            @BeforeEach
            void prepare() {
                List<ReviewDetailResponse> reviews = List.of(
                        new ReviewDetailResponse(1L, "review1", 1),
                        new ReviewDetailResponse(2L, "review1", 2),
                        new ReviewDetailResponse(3L, "review1", 3),
                        new ReviewDetailResponse(4L, "review1", 4),
                        new ReviewDetailResponse(5L, "review1", 5),
                        new ReviewDetailResponse(6L, "review1", 6),
                        new ReviewDetailResponse(7L, "review1", 7),
                        new ReviewDetailResponse(8L, "review1", 8),
                        new ReviewDetailResponse(9L, "review1", 9),
                        new ReviewDetailResponse(10L, "review1", 10)
                );
                given(reviewRepository.findByUserIdAndBetweenTimeRange(USER_ID, request))
                        .willReturn(reviews);
            }

            @Test
            @DisplayName("기분점수 통계를 리턴한다")
            void it_return_feelings_score_count() {
                ReviewStatsResponse reviewStats = reviewListService.getReviewStats(USER_ID, request);
                Map<Integer, Long> expected = Map.of(
                        1, 1L,
                        2, 1L,
                        3, 1L,
                        4, 1L,
                        5, 1L,
                        6, 1L,
                        7, 1L,
                        8, 1L,
                        9, 1L,
                        10, 1L
                );
                assertThat(reviewStats.getFeelings()).isEqualTo(expected);
            }
        }
    }
}
