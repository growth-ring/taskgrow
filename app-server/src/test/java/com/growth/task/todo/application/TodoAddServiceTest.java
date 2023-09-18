package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.service.PomodoroService;
import com.growth.task.task.domain.Tasks;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoAddServiceTest {

    @Mock
    private TodoService todoService;
    @Mock
    private PomodoroService pomodoroService;

    private TodoAddService todoAddService;

    private final Long TASK_ID = 1L;
    private final Long TODO_ID1 = 1L;
    private final String WHAT_TO_DO1 = "디자인패턴의 아름다움 스터디";
    private final int POMODORO_PERFORM_COUNT1 = 0;
    private final int POMODORO_PLAN_COUNT1 = 1;

    @BeforeEach
    void setUp() {
        todoService = mock(TodoService.class);
        pomodoroService = mock(PomodoroService.class);
        todoAddService = new TodoAddService(todoService, pomodoroService);
    }

    @Nested
    @DisplayName("save 메서드는")
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
                    .build();
            private final PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                    .performCount(POMODORO_PERFORM_COUNT1)
                    .planCount(POMODORO_PLAN_COUNT1)
                    .build();
            private final TodoAndPomodoroAddRequest todoAndPomodoroAddRequest = TodoAndPomodoroAddRequest.builder()
                    .todoAddRequest(todoAddRequest)
                    .pomodoroAddRequest(pomodoroAddRequest)
                    .build();

            @BeforeEach
            void setUp() {
                lenient().when(todoService.save(any(TodoAddRequest.class))).thenReturn(todos);
                lenient().when(pomodoroService.save(any(PomodoroAddRequest.class), any(Todos.class))).thenReturn(pomodoro);
            }

            @Test
            @DisplayName("정상적으로 저장하고 CompositeAddResponse 를 반환한다.")
            void It_shouldSaveAndReturnCompositeResponse() {
                TodoAndPomodoroAddResponse response = todoAddService.save(todoAndPomodoroAddRequest);

                assertAll(
                        () -> assertThat(response.getTodoId()).isEqualTo(TODO_ID1),
                        () -> assertThat(response.getTaskId()).isEqualTo(TASK_ID),
                        () -> assertThat(response.getTodo()).isEqualTo(WHAT_TO_DO1),
                        () -> assertThat(response.getStatus()).isEqualTo(Status.READY.toString()),
                        () -> assertThat(response.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT1),
                        () -> assertThat(response.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT1)
                );

                verify(todoService, times(1)).save(any(TodoAddRequest.class));
                verify(pomodoroService, times(1)).save(any(PomodoroAddRequest.class), any(Todos.class));
            }
        }
    }
}
