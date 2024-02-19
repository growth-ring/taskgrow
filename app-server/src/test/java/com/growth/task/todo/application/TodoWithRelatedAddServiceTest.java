package com.growth.task.todo.application;

import com.growth.task.category.domain.Category;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.service.PomodoroAddService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoWithRelatedAddServiceTest {

    @Mock
    private TodoAddService todoAddService;
    @Mock
    private PomodoroAddService pomodoroAddService;
    @Mock
    private TodoCategoryAddService todoCategoryAddService;

    private TodoWithRelatedAddService todoWithRelatedAddService;

    private final Long TASK_ID = 1L;
    private final Long TODO_ID1 = 1L;
    private final String WHAT_TO_DO1 = "디자인패턴의 아름다움 스터디";
    private final int POMODORO_PERFORM_COUNT1 = 0;
    private final int POMODORO_PLAN_COUNT1 = 1;

    @BeforeEach
    void setUp() {
        todoWithRelatedAddService = new TodoWithRelatedAddService(
                todoAddService,
                pomodoroAddService,
                todoCategoryAddService
        );
    }

    @Nested
    @DisplayName("signUp 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("Todo 및 Pomodoro 정보가 주어지면")
        class Context_whenTaskAndPomodoroExists {
            private final Tasks tasks = Tasks.builder()
                    .taskId(TASK_ID)
                    .build();
            private final Todos todos = Todos.builder()
                    .todoId(TODO_ID1)
                    .task(tasks)
                    .todo(WHAT_TO_DO1)
                    .status(Status.READY)
                    .build();
            private final Pomodoros pomodoro = Pomodoros.builder()
                    .todo(todos)
                    .performCount(POMODORO_PERFORM_COUNT1)
                    .planCount(POMODORO_PLAN_COUNT1)
                    .build();
            private final TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                    .taskId(tasks.getTaskId())
                    .todo(WHAT_TO_DO1)
                    .categoryId(1L)
                    .build();
            private final PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                    .performCount(POMODORO_PERFORM_COUNT1)
                    .planCount(POMODORO_PLAN_COUNT1)
                    .build();
            private final TodoAndPomodoroAddRequest todoAndPomodoroAddRequest = TodoAndPomodoroAddRequest.builder()
                    .todoAddRequest(todoAddRequest)
                    .pomodoroAddRequest(pomodoroAddRequest)
                    .build();

            private final Category category = Category.builder()
                    .name("스터디")
                    .build();

            @BeforeEach
            void setUp() {
                ReflectionTestUtils.setField(category, "id", 1L);

                lenient().when(todoAddService.save(any(TodoAddRequest.class))).thenReturn(todos);
                lenient().when(pomodoroAddService.save(any(PomodoroAddRequest.class), any(Todos.class))).thenReturn(pomodoro);
                lenient().when(todoCategoryAddService.save(any(Todos.class), any(Long.class))).thenReturn(new TodoCategory(todos, category));
            }

            @Test
            @DisplayName("정상적으로 저장하고 반환한다.")
            void It_shouldSaveAndReturnCompositeResponse() {
                TodoAndPomodoroAddResponse response = todoWithRelatedAddService.saveWithRelated(todoAndPomodoroAddRequest);

                assertAll(
                        () -> assertThat(response.getTodoId()).isEqualTo(TODO_ID1),
                        () -> assertThat(response.getTaskId()).isEqualTo(TASK_ID),
                        () -> assertThat(response.getTodo()).isEqualTo(WHAT_TO_DO1),
                        () -> assertThat(response.getStatus()).isEqualTo(Status.READY.toString()),
                        () -> assertThat(response.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT1),
                        () -> assertThat(response.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT1),
                        () -> assertThat(response.getCategory()).isEqualTo(category.getName()),
                        () -> verify(todoAddService, times(1)).save(any(TodoAddRequest.class)),
                        () -> verify(pomodoroAddService, times(1)).save(any(PomodoroAddRequest.class), any(Todos.class)),
                        () -> verify(todoCategoryAddService, times(1)).save(any(Todos.class), any(Long.class))
                );
            }
        }
    }
}
