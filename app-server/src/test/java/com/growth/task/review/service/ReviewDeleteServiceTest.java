package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewDeleteServiceTest {
    private static final String CONTENT = "테스트를 잘 작성합시다";
    private ReviewDeleteService reviewDeleteService;
    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewDeleteService = new ReviewDeleteService(reviewRepository);
    }

    @Nested
    @DisplayName("deleteById는")
    class Describe_deleteById {
        @Nested
        @DisplayName("존재하는 회고 아아디가 주어지면")
        class Context_with_exist_review {
            private final Tasks task = mock(Tasks.class);
            private final Review givenReview = new Review(task, CONTENT, 4);

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(givenReview, "id", 1L);
                given(reviewRepository.findById(anyLong()))
                        .willReturn(Optional.of(givenReview));
            }

            @Test
            @DisplayName("회고를 삭제한다")
            void it_delete_review() {
                reviewDeleteService.deleteById(givenReview.getId());

                verify(reviewRepository).delete(givenReview);
            }
        }

        @Nested
        @DisplayName("존재하지 않은 회고 아아디가 주어지면")
        class Context_with_not_exist_review {
            private final Tasks task = mock(Tasks.class);
            private final Review givenReview = new Review(task, CONTENT, 4);
            private final Long invalidId = 0L;

            @Test
            @DisplayName("찾을 수 없다는 예외를 던진다")
            void it_throw_not_found_exception() {
                Executable when = () -> reviewDeleteService.deleteById(invalidId);

                assertThrows(ReviewNotFoundException.class, when);
            }
        }
    }
}
