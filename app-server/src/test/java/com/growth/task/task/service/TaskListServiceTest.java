package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListResponse;
import com.growth.task.task.dto.TaskListWithTodoStatusResponse;
import com.growth.task.task.dto.TaskTodoResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.user.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.growth.task.task.service.TaskListService.collectToTask;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskListService")
class TaskListServiceTest {
    private TaskListService taskListService;
    @Mock
    private TasksRepository tasksRepository;

    @BeforeEach
    void setUp() {
        taskListService = new TaskListService(tasksRepository);
    }

    @Nested
    @DisplayName("getTasks")
    class Describe_getTasks {
        @Nested
        @DisplayName("user Id와 start_date, end_date가 넘어오면")
        class Context_with_user_id_and_start_date_and_end_date {
            Users givenUser = Users.builder()
                    .userId(1L)
                    .name("user1")
                    .password("password")
                    .build();
            Tasks givenTask = Tasks.builder()
                    .taskId(1L)
                    .taskDate(LocalDate.parse("2023-08-28"))
                    .user(givenUser)
                    .build();
            Todos givenTodo1 = Todos.builder()
                    .todoId(1L)
                    .task(givenTask)
                    .todo("api 작업")
                    .status(Status.READY)
                    .build();
            Todos givenTodo2 = Todos.builder()
                    .todoId(2L)
                    .task(givenTask)
                    .todo("과제하기")
                    .status(Status.READY)
                    .build();
            Todos givenTodo3 = Todos.builder()
                    .todoId(3L)
                    .task(givenTask)
                    .todo("오브젝트 읽기")
                    .status(Status.READY)
                    .build();
            TaskListRequest request = TaskListRequest.builder()
                    .user_id(givenUser.getUserId())
                    .start_date(LocalDate.parse("2023-08-01"))
                    .end_date(LocalDate.parse("2023-08-30"))
                    .build();

            @BeforeEach
            void prepare() {
                given(tasksRepository.findRemainedTodosByUserBetweenTimeRange(request))
                        .willReturn(List.of(
                                new TaskListWithTodoStatusResponse(1L, 1L, givenTask.getTaskDate(), givenTodo1.getStatus()),
                                new TaskListWithTodoStatusResponse(1L, 1L, givenTask.getTaskDate(), givenTodo2.getStatus()),
                                new TaskListWithTodoStatusResponse(1L, 1L, givenTask.getTaskDate(), givenTodo3.getStatus())
                        ));
            }

            @Test
            @DisplayName("task 리스트를 리턴한다")
            void it_return_task_list() {
                List<TaskListResponse> result = taskListService.getTasks(request);
                assertAll(
                        () -> assertThat(result).hasSize(1),
                        () -> assertThat(result.get(0).getTodos())
                );
            }
        }
    }

    @Nested
    @DisplayName("groupingByTask")
    class Describe_groupingByTask {

        @Nested
        @DisplayName("todo status를 가진 task 리스트가 주어지면")
        class Context_with_task_list_with_todo_status {
            List<TaskListWithTodoStatusResponse> taskList = List.of(
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(4L, 1L, LocalDate.parse("2023-08-28"), null)

            );

            @Test
            @DisplayName("task id로 status 별 개수를 카운팅한 map을 리턴한다")
            void it_return_map_grouping_by_task() {
                Map<Long, TaskTodoResponse> result = taskListService.calculateTaskTodoStatusMap(taskList);
                System.out.println(result);

                assertAll(
                        () -> assertThat(result).hasSize(4),
                        () -> assertThat(result.get(1L).getRemain()).isEqualTo(4),
                        () -> assertThat(result.get(1L).getDone()).isEqualTo(1),
                        () -> assertThat(result.get(2L).getRemain()).isEqualTo(3),
                        () -> assertThat(result.get(2L).getDone()).isZero(),
                        () -> assertThat(result.get(3L).getRemain()).isZero(),
                        () -> assertThat(result.get(3L).getDone()).isEqualTo(5),
                        () -> assertThat(result.get(4L).getRemain()).isZero(),
                        () -> assertThat(result.get(4L).getDone()).isZero()
                );
            }
        }
    }

    @Nested
    @DisplayName("calculateTodoStatus")
    class Describe_calculateTodoStatus {
        @Nested
        @DisplayName("task 리스트가 주어지면")
        class Context_with_task_list {
            @Nested
            @DisplayName("READY, PROGRESS, DONE이 골고루 존재하면")
            class Context_with_todo_status_ready_progress_done {
                List<TaskListWithTodoStatusResponse> taskList = List.of(
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.DONE)
                );

                @Test
                @DisplayName("remain 4, done 1이 나온다")
                void it_return_remain_4_and_done_1() {
                    TaskTodoResponse result = taskListService.calculateTodoStatus(taskList);
                    assertAll(
                            () -> assertThat(result.getRemain()).isEqualTo(4),
                            () -> assertThat(result.getDone()).isEqualTo(1)
                    );
                }
            }

            @Nested
            @DisplayName("READY만 존재하면")
            class Context_with_todo_status_is_only_ready {
                List<TaskListWithTodoStatusResponse> taskList = List.of(
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY)
                );

                @Test
                @DisplayName("remain 2, done 0이 나온다")
                void it_return_remain_2_and_done_0() {
                    TaskTodoResponse result = taskListService.calculateTodoStatus(taskList);
                    assertAll(
                            () -> assertThat(result.getRemain()).isEqualTo(2),
                            () -> assertThat(result.getDone()).isZero()
                    );
                }
            }

            @Nested
            @DisplayName("READY와 PROGRESS만 존재하면")
            class Context_with_todo_status_is_ready_or_progress {
                List<TaskListWithTodoStatusResponse> taskList = List.of(
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS)
                );

                @Test
                @DisplayName("remain 2, done 0이 나온다")
                void it_return_remain_2_and_done_0() {
                    TaskTodoResponse result = taskListService.calculateTodoStatus(taskList);
                    assertAll(
                            () -> assertThat(result.getRemain()).isEqualTo(2),
                            () -> assertThat(result.getDone()).isZero()
                    );
                }
            }

            @Nested
            @DisplayName("DONE만 존재하면")
            class Context_with_todo_status_is_only_done {
                List<TaskListWithTodoStatusResponse> taskList = List.of(
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.DONE)
                );

                @Test
                @DisplayName("remain 0, done 2이 나온다")
                void it_return_remain_0_and_done_2() {
                    TaskTodoResponse result = taskListService.calculateTodoStatus(taskList);
                    assertAll(
                            () -> assertThat(result.getRemain()).isZero(),
                            () -> assertThat(result.getDone()).isEqualTo(2)
                    );
                }
            }

            @Nested
            @DisplayName("존재하지 않다면")
            class Context_with_todo_status_is_null {
                List<TaskListWithTodoStatusResponse> taskList = List.of(
                        new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), null)
                );

                @Test
                @DisplayName("remain 0, done 0이 나온다")
                void it_return_remain_0_and_done_0() {
                    TaskTodoResponse result = taskListService.calculateTodoStatus(taskList);
                    assertAll(
                            () -> assertThat(result.getRemain()).isZero(),
                            () -> assertThat(result.getDone()).isZero()
                    );
                }
            }
        }
    }

    @Nested
    @DisplayName("collectToTask")
    class Describe_collectToTask {
        @Nested
        @DisplayName("테스크 리스트와 todos 진행률 Map이 주어지면")
        class Context_wiht_task_list_and_todos_status_map {
            List<TaskListWithTodoStatusResponse> taskList = List.of(
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.PROGRESS),
                    new TaskListWithTodoStatusResponse(1L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(2L, 1L, LocalDate.parse("2023-08-28"), Status.READY),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(3L, 1L, LocalDate.parse("2023-08-28"), Status.DONE),
                    new TaskListWithTodoStatusResponse(4L, 1L, LocalDate.parse("2023-08-28"), null)

            );
            Map<Long, TaskTodoResponse> groupingByTask = Map.of(
                    1L, new TaskTodoResponse(4, 1),
                    2L, new TaskTodoResponse(3, 0),
                    3L, new TaskTodoResponse(0, 2),
                    4L, new TaskTodoResponse(0, 0)
            );

            @Test
            @DisplayName("중복을 제거하고 매핑한 리스트를 리턴한다")
            void it_return_task_list() {
                List<TaskListResponse> result = collectToTask(taskList, groupingByTask);
                assertAll(
                        () -> assertThat(result).hasSize(4)
                );
            }
        }
    }
}
