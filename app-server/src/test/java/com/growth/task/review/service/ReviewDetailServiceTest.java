package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.exception.ReviewNotFoundException;
import com.growth.task.task.domain.Tasks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewDetailServiceTest {
    private ReviewDetailService reviewDetailService;
    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewDetailService = new ReviewDetailService(reviewRepository);
    }

    @Nested
    @DisplayName("findReviewById는")
    class Describe_findReviewById {
        @Nested
        @DisplayName("존재하는 회고 아이디가 주어지면")
        class Context_with_exist_review_id {
            private Review review = Review.builder()
                    .tasks(mock(Tasks.class))
                    .contents("모든 Todo를 끝내서 기분이 좋다")
                    .feelingsScore(10)
                    .build();

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(review, "id", 1L);
                given(reviewRepository.findById(review.getId()))
                        .willReturn(Optional.of(review));
            }

            @Test
            @DisplayName("회고를 조회해 리턴한다")
            void it_return_review() {
                Review actual = reviewDetailService.findReviewById(review.getId());
                assertAll(
                        () -> assertThat(actual.getContents()).isEqualTo(review.getContents()),
                        () -> assertThat(actual.getFeelingsScore()).isEqualTo(review.getFeelingsScore())
                );
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회고 아이디가 주어지면")
        class Context_with_not_exist_review_id {
            private final Long invalidId = 0L;

            @Test
            @DisplayName("찾을 수 없다는 예외를 던진다")
            void it_return_review() {
                Executable when = () -> reviewDetailService.findReviewById(invalidId);
                assertThrows(ReviewNotFoundException.class, when);
            }
        }
    }

    @Nested
    @DisplayName("getReview는")
    class Describe_getReview {
        @Nested
        @DisplayName("존재하는 회고 아이디가 주어지면")
        class Context_with_exist_review_id {
            private Review review = Review.builder()
                    .tasks(mock(Tasks.class))
                    .contents("모든 Todo를 끝내서 기분이 좋다")
                    .feelingsScore(10)
                    .build();

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(review, "id", 1L);
                given(reviewRepository.findById(review.getId()))
                        .willReturn(Optional.of(review));
            }

            @Test
            @DisplayName("회고를 조회해 리턴한다")
            void it_return_review() {
                ReviewDetailResponse actual = reviewDetailService.getReview(review.getId());
                assertAll(
                        () -> assertThat(actual.getContents()).isEqualTo(review.getContents()),
                        () -> assertThat(actual.getFeelingsScore()).isEqualTo(review.getFeelingsScore())
                );
            }
        }
    }
}
