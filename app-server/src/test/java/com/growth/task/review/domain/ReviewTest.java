package com.growth.task.review.domain;

import com.growth.task.review.exception.OutOfBoundsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Review")
class ReviewTest {
    @Test
    @DisplayName("1~10 사이의 숫자가 주어지면 회고를 생성한다")
    void createReview() {
        Review review = Review.builder()
                .contents("회고를 작성합니다.")
                .feelingsScore(3)
                .build();

        assertThat(review).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11})
    @DisplayName("1~10사이가 아닌 경우 예외가 발생한다")
    void outBoundFeelingScoreReviewCreate(int score) {
        Executable when = () -> Review.builder()
                .contents("회고를 작성합니다.")
                .feelingsScore(score)
                .build();

        assertThrows(OutOfBoundsException.class, when);
    }
}
