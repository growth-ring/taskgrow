package com.growth.task.todo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.service.PomodoroService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateResponse;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TodoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TodoUpdateServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    private TodoService todoService;

    private PomodoroService pomodoroService;

    private TodoUpdateService todoUpdateService;

    @BeforeEach
    void setUp() {
        todoService = mock(TodoService.class);
        pomodoroService = mock(PomodoroService.class);
        todoUpdateService = new TodoUpdateService(todoService, pomodoroService);
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("존재하지 않는 todoId 가 주어지면")
        class Context_whenTodoIdDoesNotExist {
            private final Long TODO_ID1 = 1L;
            private final TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest = TodoAndPomodoroUpdateRequest.builder().build();

            @BeforeEach
            void setUp() {
                when(todoService.update(TODO_ID1, todoAndPomodoroUpdateRequest.getTodoUpdateRequest()))
                        .thenThrow(TodoNotFoundException.class);
            }

            @Test
            @DisplayName("TodoNotFoundException 예외를 던진다")
            void It_throws_TodoNotFoundException() {
                assertThrows(TodoNotFoundException.class, () -> {
                    todoUpdateService.update(TODO_ID1, todoAndPomodoroUpdateRequest);
                });
            }
        }

        @Nested
        @DisplayName("Todo 및 Pomodoro 정보가 주어지면")
        class Context_whenTaskAndPomodoroExists {
            private final Long TASK_ID = 1L;
            private final Long TODO_ID1 = 1L;
            private final String NEW_WHAT_TO_DO = "쿠버네티스 입문";
            private final int NEW_POMODORO_PERFORM_COUNT = 0;
            private final int NEW_POMODORO_PLAN_COUNT = 5;

            private final Tasks tasks = Tasks.builder()
                    .taskId(TASK_ID)
                    .build();
            private final Todos todos = Todos.builder()
                    .todoId(TODO_ID1)
                    .task(tasks)
                    .todo(NEW_WHAT_TO_DO)
                    .status(Status.PROGRESS)
                    .build();
            private final Pomodoros pomodoro = Pomodoros.builder()
                    .todo(todos)
                    .performCount(NEW_POMODORO_PERFORM_COUNT)
                    .planCount(NEW_POMODORO_PLAN_COUNT)
                    .build();
            private final TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder()
                    .taskId(tasks.getTaskId())
                    .todo(NEW_WHAT_TO_DO)
                    .build();
            private final PomodoroUpdateRequest pomodoroUpdateRequest = PomodoroUpdateRequest.builder()
                    .performCount(NEW_POMODORO_PERFORM_COUNT)
                    .planCount(NEW_POMODORO_PLAN_COUNT)
                    .build();
            private final TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest = TodoAndPomodoroUpdateRequest.builder()
                    .todoUpdateRequest(todoUpdateRequest)
                    .pomodoroUpdateRequest(pomodoroUpdateRequest)
                    .build();

            @BeforeEach
            void setUp() {
                lenient().when(tasksRepository.findById(TASK_ID)).thenReturn(Optional.of(tasks));
                lenient().when(todoService.update(TODO_ID1, todoAndPomodoroUpdateRequest.getTodoUpdateRequest())).thenReturn(todos);
                lenient().when(pomodoroService.update(TODO_ID1, todoAndPomodoroUpdateRequest.getPomodoroUpdateRequest())).thenReturn(pomodoro);
            }

            @Test
            @DisplayName("정상적으로 저장하고 TodoAndPomodoroUpdateResponse 를 반환한다.")
            void It_shouldSaveAndReturnTodoAndPomodoroUpdateResponse() {
                TodoAndPomodoroUpdateResponse response = todoUpdateService.update(TODO_ID1, todoAndPomodoroUpdateRequest);

                assertAll(
                        () -> assertThat(response.getTodoUpdateResponse().getTodo()).isEqualTo(NEW_WHAT_TO_DO),
                        () -> assertThat(response.getTodoUpdateResponse().getStatus()).isEqualTo(Status.READY),
                        () -> assertThat(response.getPomodoroUpdateResponse().getPerformCount()).isEqualTo(NEW_POMODORO_PERFORM_COUNT),
                        () -> assertThat(response.getPomodoroUpdateResponse().getPlanCount()).isEqualTo(NEW_POMODORO_PLAN_COUNT)
                );
            }
        }
    }
}
