package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {
    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.by("id").descending());

    private static final int DONE_COUNT = 5;
    private static final int PROGRESS_COUNT = 2;
    private static final int UNDONE_COUNT = 5;
    private static final long USER_ID = 1L;
    private static final LocalDate LOCAL_DATE_11_20 = LocalDate.of(2023, 11, 20);
    private static final LocalDate LOCAL_DATE_11_19 = LocalDate.of(2023, 11, 19);
    private static final int TOTAL_COUNT = 10;
    private static final int STATS_DONE = 7;
    private static final int STATS_PROGRESS = 2;
    private static final int STATS_UNDONE = 3;
    @Mock
    private TodosRepository todosRepository;
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
        todoListService = new TodoListService(todosRepository);
    }

    @DisplayName("getTodosByTaskId 메서드는")
    @Nested
    class Describe_getTodosByTaskId {
        private final Tasks tasks = Tasks.builder()
                .taskId(1L)
                .build();

        @Nested
        @DisplayName("taskId 가 존재하지만 Todos 가 없는 경우")
        class Context_whenTaskIdExistsButNoTodos {

            @BeforeEach
            void setup() {
                given(todosRepository.findTodoWithPomodoroByTaskId(TASK_ID))
                        .willReturn(Collections.emptyList());
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void It_returns_emptyList() {
                List<TodoWithPomodoroResponse> result = todoListService.getTodosByTaskId(TASK_ID);
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
                List<TodoWithPomodoroResponse> todoWithPomodoro = List.of(
                        new TodoWithPomodoroResponse(todos1, pomodoro1),
                        new TodoWithPomodoroResponse(todos2, pomodoro2)
                );

                given(todosRepository.findTodoWithPomodoroByTaskId(tasks.getTaskId()))
                        .willReturn(todoWithPomodoro);
            }

            @Test
            @DisplayName("TodoWithPomodoroResponse 리스트를 반환한다.")
            void it_return_TodoWithPomodoroResponse() {
                List<TodoWithPomodoroResponse> result = todoListService.getTodosByTaskId(TASK_ID);
                TodoWithPomodoroResponse firstResponse = result.get(0);
                TodoWithPomodoroResponse secondResponse = result.get(1);

                assertAll(
                        () -> assertThat(result.size()).isEqualTo(2),
                        () -> assertThat(firstResponse.getTodoId()).isEqualTo(TODO_ID1),
                        () -> assertThat(firstResponse.getTaskId()).isEqualTo(TASK_ID),
                        () -> assertThat(firstResponse.getTodo()).isEqualTo(WHAT_TO_DO1),
                        () -> assertThat(firstResponse.getStatus()).isEqualTo(Status.READY),
                        () -> assertThat(firstResponse.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT1),
                        () -> assertThat(firstResponse.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT1),
                        () -> assertThat(secondResponse.getTodoId()).isEqualTo(TODO_ID2),
                        () -> assertThat(secondResponse.getTaskId()).isEqualTo(TASK_ID),
                        () -> assertThat(secondResponse.getTodo()).isEqualTo(WHAT_TO_DO2),
                        () -> assertThat(secondResponse.getStatus()).isEqualTo(Status.PROGRESS),
                        () -> assertThat(secondResponse.getPerformCount()).isEqualTo(POMODORO_PERFORM_COUNT2),
                        () -> assertThat(secondResponse.getPlanCount()).isEqualTo(POMODORO_PLAN_COUNT2)
                );
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

    @Nested
    @DisplayName("getTodoByUserAndParams")
    class Describe_getTodoByUserAndParams {
        @Nested
        @DisplayName("사용자 아이디와 status가 주어지면")
        class Context_with_userId {
            private TodoListRequest request = new TodoListRequest(Status.READY.name());

            @BeforeEach
            void prepare() {
                Page page = new PageImpl(List.of(
                        new TodoDetailResponse("책읽기", Status.READY, 0, 3, LocalDate.of(2023, 12, 01)),
                        new TodoDetailResponse("소설읽기", Status.READY, 0, 3, LocalDate.of(2023, 12, 01)),
                        new TodoDetailResponse("책읽기", Status.READY, 0, 3, LocalDate.of(2023, 12, 01)),
                        new TodoDetailResponse("운동하기", Status.READY, 0, 3, LocalDate.of(2023, 12, 01))
                ));
                given(todosRepository.findAllByUserAndParams(DEFAULT_PAGEABLE, USER_ID, request))
                        .willReturn(page);
            }

            @Test
            @DisplayName("투두 내용, 상태, 뽀모도로 개수, 테스크 날짜를 리턴한다")
            void it_return_todo_list() {
                Page<TodoDetailResponse> todos = todoListService.getTodoByUserAndParams(DEFAULT_PAGEABLE, USER_ID, request);

                assertAll(
                        () -> assertThat(todos).hasSize(4),
                        () -> assertThat(todos).extracting(TodoDetailResponse::getStatus).containsAnyOf(Status.READY)
                );
            }
        }
    }
}
