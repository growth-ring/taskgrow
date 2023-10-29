package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewUpdateRequest;
import com.growth.task.review.exception.ReviewNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewUpdateService")
class ReviewUpdateServiceTest {
    public static final String CONTENTS = "회고를 작성했습니다.";
    public static final int FEELINGS_SCORE = 5;
    public static final long REVIEW_ID = 1L;
    public static final String NEW_CONTENTS = "회고 내용을 수정했습니다.";
    public static final int NEW_FEELINGS_SCORE = 7;
    private ReviewUpdateService reviewUpdateService;
    @Mock
    private ReviewDetailService reviewDetailService;

    @BeforeEach
    void setUp() {
        reviewUpdateService = new ReviewUpdateService(reviewDetailService);
    }

    @Nested
    @DisplayName("update")
    class Describe_update {
        @Nested
        @DisplayName("존재하는 회고의 아이디와 회고 내용, 기분점수가 주어지면")
        class Context_with_exist_review_and_content_and_feeling_score {
            private final Review review = Review.builder()
                    .contents(CONTENTS)
                    .feelingsScore(FEELINGS_SCORE)
                    .build();

            private final ReviewUpdateRequest request = new ReviewUpdateRequest(NEW_CONTENTS, NEW_FEELINGS_SCORE);

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(review, "id", REVIEW_ID);
                given(reviewDetailService.findReviewById(REVIEW_ID))
                        .willReturn(review);
            }

            @Test
            @DisplayName("내용과 기분점수를 업데이트한다")
            void update_content_and_feeling_score() {
                Review actual = reviewUpdateService.update(REVIEW_ID, request);

                assertAll(
                        () -> assertThat(actual.getContents()).isEqualTo(NEW_CONTENTS),
                        () -> assertThat(actual.getFeelingsScore()).isEqualTo(NEW_FEELINGS_SCORE)
                );
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회고 아이디가 주어지면")
        class Context_with_not_exist_review_id {
            private final Long invalidId = 0L;
            private final ReviewUpdateRequest request = new ReviewUpdateRequest(NEW_CONTENTS, NEW_FEELINGS_SCORE);

            @BeforeEach
            void prepare() {
                given(reviewDetailService.findReviewById(invalidId))
                        .willThrow(ReviewNotFoundException.class);
            }

            @Test
            @DisplayName("찾을 수 없다는 예외를 던진다")
            void it_return_review() {
                Executable when = () -> reviewUpdateService.update(invalidId, request);
                assertThrows(ReviewNotFoundException.class, when);
            }
        }
    }
}
