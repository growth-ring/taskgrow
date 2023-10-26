package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.dto.ReviewAddResponse;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewAddServiceTest {
    public static final String CONTENT = "회고 내용";
    private ReviewAddService reviewAddService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TaskDetailService taskDetailService;

    @BeforeEach
    void setUp() {
        reviewAddService = new ReviewAddService(reviewRepository, taskDetailService);
    }

    @Nested
    @DisplayName("save")
    class Describe_save {
        @Nested
        @DisplayName("존재하는 테스트와 회고 내용과 기분점수가 주어지면")
        class Context_with_task_content_feeling_score {
            private final Tasks task = mock(Tasks.class);
            private final ReviewAddRequest request = new ReviewAddRequest(task.getTaskId(), CONTENT, 4);
            private final Review givenReview = new Review(task, CONTENT, 4);

            @BeforeEach
            void prepare() {
                given(reviewRepository.save(any(Review.class)))
                        .willReturn(givenReview);
            }

            @Test
            @DisplayName("회고를 생성한다")
            void it_create_review() {
                ReviewAddResponse actual = reviewAddService.save(request);

                assertThat(actual).isNotNull();
            }
        }
    }
}
