package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.dto.ReviewAddResponse;
import com.growth.task.review.exception.InvalidReviewRequestException;
import com.growth.task.review.repository.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.user.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.growth.task.review.exception.InvalidReviewRequestException.DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewAddServiceTest {
    private static final String CONTENT = "회고 내용";
    private static final LocalDate TASK_DATE_12_5 = LocalDate.of(2023, 12, 5);
    private ReviewAddService reviewAddService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TaskDetailService taskDetailService;
    @Mock
    private TodosRepository todosRepository;

    @BeforeEach
    void setUp() {
        reviewAddService = new ReviewAddService(
                reviewRepository,
                taskDetailService,
                todosRepository
        );
    }

    @Nested
    @DisplayName("saveWithRelated")
    class Describe_save {
        @Nested
        @DisplayName("Todo가 존재하는 테스크와 회고 내용과 기분점수가 주어지면")
        class Context_with_existed_todo_in_task_content_feeling_score {
            private final Tasks task = mock(Tasks.class);
            private final ReviewAddRequest request = new ReviewAddRequest(task.getTaskId(), CONTENT, 4);
            private final Review givenReview = new Review(task, CONTENT, 4);

            @BeforeEach
            void prepare() {
                given(taskDetailService.findTaskById(any()))
                        .willReturn(task);
                given(reviewRepository.save(any(Review.class)))
                        .willReturn(givenReview);
                given(todosRepository.existsByTask_TaskId(any()))
                        .willReturn(true);
            }

            @Test
            @DisplayName("회고를 생성한다")
            void it_create_review() {
                ReviewAddResponse actual = reviewAddService.save(request);

                assertThat(actual).isNotNull();
            }
        }

        @Nested
        @DisplayName("Todo가 존재하자 않는 테스크와 회고 내용과 기분점수가 주어지면")
        class Context_with_not_existed_todo_in_task_content_feeling_score {
            private final Tasks task = new Tasks(1L, mock(Users.class), TASK_DATE_12_5);
            private final ReviewAddRequest request = new ReviewAddRequest(task.getTaskId(), CONTENT, 4);

            @BeforeEach
            void prepare() {
                given(taskDetailService.findTaskById(any()))
                        .willReturn(task);
                given(todosRepository.existsByTask_TaskId(any()))
                        .willReturn(false);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> reviewAddService.save(request))
                        .isInstanceOf(InvalidReviewRequestException.class)
                        .hasMessageContaining(String.format(DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE, TASK_DATE_12_5));
            }
        }
    }
}
