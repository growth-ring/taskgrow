package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.service.PomodoroAddService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.response.TodoListResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    public static final int DONE_COUNT = 5;
    public static final int PROGRESS_COUNT = 2;
    public static final int UNDONE_COUNT = 5;
    public static final long USER_ID = 1L;
    public static final LocalDate LOCAL_DATE_11_20 = LocalDate.of(2023, 11, 20);
    public static final LocalDate LOCAL_DATE_11_19 = LocalDate.of(2023, 11, 19);
    public static final int TOTAL_COUNT = 10;
    public static final int STATS_DONE = 7;
    public static final int STATS_PROGRESS = 2;
    public static final int STATS_UNDONE = 3;
    @Mock
    private TodosRepository todosRepository;

    @Mock
    private PomodorosRepository pomodorosRepository;

    @Mock
    private TasksRepository tasksRepository;

    private TodoService todoService;
    private PomodoroAddService pomodoroAddService;
    private TodoListService todoListService;

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
        pomodoroAddService = mock(PomodoroAddService.class);
        todoListService = new TodoListService(todosRepository, pomodorosRepository, tasksRepository);
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
                assertThrows(TaskNotFoundException.class, () -> todoListService.getTodosByTaskId(TASK_ID));
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
                List<TodoListResponse> result = todoListService.getTodosByTaskId(TASK_ID);
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
                List<TodoListResponse> result = todoListService.getTodosByTaskId(TASK_ID);

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

    @DisplayName("aggregate")
    @Nested
    class Describe_aggregate {
        @DisplayName("todo 리스트가 주어지면")
        @Nested
        class Context_with_todo_list {
            private static List<TodoResponse> todos = List.of(
                    new TodoResponse(1L, 1L, "책읽기", Status.READY),
                    new TodoResponse(1L, 1L, "소설읽기", Status.DONE),
                    new TodoResponse(1L, 2L, "책읽기", Status.DONE),
                    new TodoResponse(1L, 2L, "운동하기", Status.DONE),
                    new TodoResponse(1L, 2L, "스터디하기", Status.DONE),
                    new TodoResponse(1L, 3L, "밥먹기", Status.PROGRESS),
                    new TodoResponse(1L, 3L, "책읽기", Status.DONE),
                    new TodoResponse(1L, 4L, "책읽기", Status.READY),
                    new TodoResponse(1L, 4L, "쓰레기버리기", Status.READY),
                    new TodoResponse(1L, 4L, "계획짜기", Status.PROGRESS)
            );

            @DisplayName("총 투두 개수, 완료한 투두 개수, 진행중인 투두 개수, 미완료 투두 개수를 계산한다")
            @Test
            void it_aggregate() {
                TodoStatsResponse actual = todoListService.aggregate(todos);

                assertAll(
                        () -> assertThat(actual.getTotalCount()).isEqualTo(todos.size()),
                        () -> assertThat(actual.getDoneCount()).isEqualTo(DONE_COUNT),
                        () -> assertThat(actual.getProgressCount()).isEqualTo(PROGRESS_COUNT),
                        () -> assertThat(actual.getUndoneCount()).isEqualTo(UNDONE_COUNT)
                );
            }
        }
    }

    @Nested
    @DisplayName("getTodoCount")
    class Describe_getTodoCount {
        @Nested
        @DisplayName("사용자 아이디가 주어지면")
        class Context_with_userId {
            private TodoStatsRequest request = new TodoStatsRequest(LOCAL_DATE_11_19, LOCAL_DATE_11_20);

            @BeforeEach
            void prepare() {
                List<TodoResponse> todos = List.of(
                        new TodoResponse(1L, 1L, "책읽기", Status.READY),
                        new TodoResponse(1L, 1L, "소설읽기", Status.DONE),
                        new TodoResponse(1L, 2L, "책읽기", Status.DONE),
                        new TodoResponse(1L, 2L, "운동하기", Status.DONE),
                        new TodoResponse(1L, 2L, "스터디하기", Status.DONE),
                        new TodoResponse(1L, 3L, "밥먹기", Status.PROGRESS),
                        new TodoResponse(1L, 3L, "책읽기", Status.DONE),
                        new TodoResponse(1L, 4L, "책읽기", Status.DONE),
                        new TodoResponse(1L, 4L, "쓰레기버리기", Status.DONE),
                        new TodoResponse(1L, 4L, "계획짜기", Status.PROGRESS)
                );
                given(todosRepository.findByUserIdAndBetweenTimeRange(USER_ID, request))
                        .willReturn(todos);
            }

            @Test
            @DisplayName("총 투두 개수, 완료한 투두 개수, 진행중인 투두 개수, 미완료 투두 개수를 리턴한다")
            void it_return_stats_todo() {
                TodoStatsResponse todoStats = todoListService.getTodoStats(USER_ID, request);

                assertAll(
                        () -> assertThat(todoStats.getTotalCount()).isEqualTo(TOTAL_COUNT),
                        () -> assertThat(todoStats.getDoneCount()).isEqualTo(STATS_DONE),
                        () -> assertThat(todoStats.getProgressCount()).isEqualTo(STATS_PROGRESS),
                        () -> assertThat(todoStats.getUndoneCount()).isEqualTo(STATS_UNDONE)

                );
            }
        }
    }
}
