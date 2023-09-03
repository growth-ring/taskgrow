package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.service.PomodoroService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.todo.dto.response.TodoListResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodosServiceTest {

    @Mock
    private TodosRepository todosRepository;

    @Mock
    private PomodorosRepository pomodorosRepository;

    @Mock
    private TasksRepository tasksRepository;

    private TodoService todoService;

    private PomodoroService pomodoroService;

    private TodosService todosService;

    private final Long TASK_ID = 1L;
    private final Long TODO_ID1 = 1L;
    private final Long TODO_ID2 = 2L;
    private final Long POMODORO_ID1 = 1L;
    private final Long POMODORO_ID2 = 2L;
    private final String WHAT_TO_DO1 = "디자인패턴의 아름다움 스터디";
    private final String WHAT_TO_DO2 = "프로젝트 진행하기";
    private final int POMODORO_PERFORM_COUNT1 = 0;
    private final int POMODORO_PERFORM_COUNT2 = 1;
    private final int POMODORO_PLAN_COUNT1 = 1;
    private final int POMODORO_PLAN_COUNT2 = 2;

    @BeforeEach
    void setUp() {
        todoService = mock(TodoService.class);
        pomodoroService = mock(PomodoroService.class);
        todosService = new TodosService(todosRepository, pomodorosRepository, tasksRepository, todoService, pomodoroService);
    }


    @DisplayName("getTodosByTaskId 메서드는")
    @Nested
    class Describe_getTodosByTaskId {
        private final Tasks tasks = Tasks.builder()
                .taskId(1L)
                .build();

        @Nested
        @DisplayName("taskId 로 Tasks 가 확인되지 않는 경우")
        class Context_whenTaskIdDoesNotExist {

            @BeforeEach
            void setup() {
                given(tasksRepository.existsById(TASK_ID)).willReturn(false);
            }

            @Test
            @DisplayName("TaskNotFoundException 오류를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> todosService.getTodosByTaskId(TASK_ID));
            }
        }

        @Nested
        @DisplayName("taskId 가 존재하지만 Todos 가 없는 경우")
        class Context_whenTaskIdExistsButNoTodos {

            @BeforeEach
            void setup() {
                given(tasksRepository.existsById(TASK_ID)).willReturn(true);
                given(todosRepository.findByTask_TaskId(TASK_ID)).willReturn(Collections.emptyList());
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void It_returns_emptyList() {
                List<TodoListResponse> result = todosService.getTodosByTaskId(TASK_ID);
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("유효한 taskId 와 Todos 가 있는 경우")
        class Context_withValidTaskIdAndTodos {
            private final Todos todos1 = Todos.builder()
                    .todoId(TODO_ID1)
                    .task(tasks)
                    .todo(WHAT_TO_DO1)
                    .status(Status.READY)
                    .build();
            private final Todos todos2 = Todos.builder()
                    .todoId(TODO_ID2)
                    .task(tasks)
                    .todo(WHAT_TO_DO2)
                    .status(Status.PROGRESS)
                    .build();
            private final Pomodoros pomodoro1 = Pomodoros.builder()
                    .pomodoroId(POMODORO_ID1)
                    .todo(todos1)
                    .performCount(POMODORO_PERFORM_COUNT1)
                    .planCount(POMODORO_PLAN_COUNT1)
                    .build();
            private final Pomodoros pomodoro2 = Pomodoros.builder()
                    .pomodoroId(POMODORO_ID2)
                    .todo(todos2)
                    .performCount(POMODORO_PERFORM_COUNT2)
                    .planCount(POMODORO_PLAN_COUNT2)
                    .build();

            @BeforeEach
            void setUp() {
                List<Todos> mockedTodos = Arrays.asList(todos1, todos2);
                List<Pomodoros> mockedPomodoros = Arrays.asList(pomodoro1, pomodoro2);
                List<Long> todoIds = Arrays.asList(TODO_ID1, TODO_ID2);

                given(tasksRepository.existsById(TASK_ID)).willReturn(true);
                given(todosRepository.findByTask_TaskId(TASK_ID)).willReturn(mockedTodos);
                given(pomodorosRepository.findAllByTodo_TodoIdIn(todoIds)).willReturn(mockedPomodoros);
            }

            @Test
            @DisplayName("TodoGetResponse 리스트를 반환한다.")
            void It_shouldReturnTodoGetResponseList() {
                List<TodoListResponse> result = todosService.getTodosByTaskId(TASK_ID);

                assertThat(result.size()).isEqualTo(2);

                TodoListResponse firstResponse = result.get(0);
                assertThat(firstResponse.getTodoId()).isEqualTo(TODO_ID1);
                assertThat(firstResponse.getTaskId()).isEqualTo(TASK_ID);
                assertThat(firstResponse.getTodo()).isEqualTo(WHAT_TO_DO1);
                assertThat(firstResponse.getStatus()).isEqualTo(Status.READY);
                assertThat(firstResponse.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT1);
                assertThat(firstResponse.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT1);

                TodoListResponse secondResponse = result.get(1);
                assertThat(secondResponse.getTodoId()).isEqualTo(TODO_ID2);
                assertThat(secondResponse.getTaskId()).isEqualTo(TASK_ID);
                assertThat(secondResponse.getTodo()).isEqualTo(WHAT_TO_DO2);
                assertThat(secondResponse.getStatus()).isEqualTo(Status.PROGRESS);
                assertThat(secondResponse.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT2);
                assertThat(secondResponse.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT2);
            }
        }
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
            private final Todos todos1 = Todos.builder()
                    .todoId(TODO_ID1)
                    .task(tasks)
                    .todo(WHAT_TO_DO1)
                    .status(Status.READY)
                    .build();
            private final Pomodoros pomodoro1 = Pomodoros.builder()
                    .todo(todos1)
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
                lenient().when(tasksRepository.findById(TASK_ID)).thenReturn(Optional.of(tasks));
                lenient().when(todoService.save(todoAndPomodoroAddRequest.getTodoAddRequest())).thenReturn(todos1);
                lenient().when(pomodoroService.save(todoAndPomodoroAddRequest.getPomodoroAddRequest(), todos1)).thenReturn(pomodoro1);
            }

            @Test
            @DisplayName("정상적으로 저장하고 CompositeAddResponse 를 반환한다.")
            void It_shouldSaveAndReturnCompositeResponse() {
                TodoAndPomodoroAddResponse response = todosService.save(todoAndPomodoroAddRequest);

                assertAll(
                        () -> assertThat(response.getTodoAddResponse().getTodoId()).isEqualTo(TODO_ID1),
                        () -> assertThat(response.getTodoAddResponse().getTaskId()).isEqualTo(TASK_ID),
                        () -> assertThat(response.getTodoAddResponse().getTodo()).isEqualTo(WHAT_TO_DO1),
                        () -> assertThat(response.getTodoAddResponse().getStatus()).isEqualTo(Status.READY),
                        () -> assertThat(response.getPomodoroAddResponse().getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT1),
                        () -> assertThat(response.getPomodoroAddResponse().getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT1)
                );
            }
        }
    }
}
