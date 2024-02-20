package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoUpdateOrder;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TodoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoUpdateService")
class TodoUpdateServiceTest {
    public static final String CATEGORY_STURY = "공부";
    private final Long TASK_ID1 = 1L;
    private final String WHAT_TO_DO = "디자인패턴의 아름다움 스터디";
    private final String NEW_WHAT_TO_DO = "쿠버네티스 입문";
    private TodoUpdateService todoUpdateService;
    @Mock
    private TodoDetailService todoDetailService;
    @Mock
    private TodoListService todoListService;

    @Mock
    private TodoCategoryUpdateService todoCategoryUpdateService;

    @BeforeEach
    void setUp() {
        todoUpdateService = new TodoUpdateService(todoDetailService, todoListService, todoCategoryUpdateService);
    }

    @Nested
    @DisplayName("todoUpdateService 의 update 메서드는")
    class Describe_update {
        private final Long TODO_ID1 = 1L;

        @Nested
        @DisplayName("todoId 로 todos 가 확인되는 경우")
        class Context_whenTodoIdExists {
            private final TodoUpdateRequest todoUpdateRequest_todo = TodoUpdateRequest.builder()
                    .todo(NEW_WHAT_TO_DO)
                    .build();

            private final TodoUpdateRequest todoUpdateRequest_status = TodoUpdateRequest.builder()
                    .status(Status.DONE)
                    .build();

            private final TodoUpdateRequest todoUpdateRequest_todo_and_status = TodoUpdateRequest.builder()
                    .todo(NEW_WHAT_TO_DO)
                    .status(Status.DONE)
                    .build();

            private final TodoUpdateRequest todoUpdateRequestCategory = TodoUpdateRequest.builder()
                    .categoryId(1L)
                    .build();
            private final Tasks tasks = Tasks.builder()
                    .taskId(TASK_ID1)
                    .build();

            private final Todos todos = Todos.builder()
                    .todoId(TODO_ID1)
                    .task(tasks)
                    .todo(WHAT_TO_DO)
                    .status(Status.PROGRESS)
                    .build();

            @BeforeEach
            void prepare() {
                given(todoDetailService.getTodo(TODO_ID1))
                        .willReturn(todos);
            }

            @Test
            @DisplayName("todo 에 대한 TodoUpdateRequest 가 저장된다.")
            void it_updateTheTodo() {
                Todos todos = todoUpdateService.update(TODO_ID1, todoUpdateRequest_todo);

                assertAll(
                        () -> assertThat(todos.getTodo()).isEqualTo(NEW_WHAT_TO_DO),
                        () -> assertThat(todos.getStatus()).isEqualTo(Status.PROGRESS)
                );
            }

            @Test
            @DisplayName("status 에 대한 TodoUpdateRequest 가 저장된다.")
            void It_updateTheStatus() {
                Todos todos = todoUpdateService.update(TODO_ID1, todoUpdateRequest_status);
                assertAll(
                        () -> assertThat(todos.getTodo()).isEqualTo(WHAT_TO_DO),
                        () -> assertThat(todos.getStatus()).isEqualTo(Status.DONE)
                );
            }

            @Test
            @DisplayName("todo 와 status 에 대한 TodoUpdateRequest 가 저장된다.")
            void It_updateTheTodoAndStatus() {
                Todos todos = todoUpdateService.update(TODO_ID1, todoUpdateRequest_todo_and_status);

                assertAll(
                        () -> assertThat(todos.getTodo()).isEqualTo(NEW_WHAT_TO_DO),
                        () -> assertThat(todos.getStatus()).isEqualTo(Status.DONE)
                );
            }

            @Test
            @DisplayName("category가 수정된다")
            void it_update_category() {
                Todos todos = todoUpdateService.update(TODO_ID1, todoUpdateRequestCategory);

                assertAll(
                        () -> assertThat(todos.getTodo()).isEqualTo(WHAT_TO_DO),
                        () -> assertThat(todos.getStatus()).isEqualTo(Status.PROGRESS),
                        () -> verify(todoCategoryUpdateService, times(1)).update(any(), any())
                );
            }
        }

        @Nested
        @DisplayName("todoId 로 Todos 가 확인되지 않는 경우")
        class Context_whenTodoIdDoesNotExist {
            private final TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder()
                    .todo(NEW_WHAT_TO_DO)
                    .build();

            @BeforeEach
            void setUp() {
                given(todoDetailService.getTodo(TODO_ID1))
                        .willThrow(TodoNotFoundException.class);
            }

            @Test
            @DisplayName("TodoNotFoundException 오류를 던진다.")
            void It_throws_TodoNotFoundException() {
                Executable when = () -> todoUpdateService.update(TODO_ID1, todoUpdateRequest);

                assertThrows(TodoNotFoundException.class, when);
            }
        }
    }

    @Nested
    @DisplayName("updateOrder")
    class Describe_updateOrder {
        @Nested
        @DisplayName("order 수정 요청 리스트가 주어지면")
        class Context_with_request_list {
            private final Tasks task = Tasks.builder()
                    .taskId(TASK_ID1)
                    .build();
            private final Todos todo1 = Todos.builder()
                    .todoId(1L)
                    .todo("공부하기")
                    .status(Status.READY)
                    .orderNo(1)
                    .task(task)
                    .build();
            private final Todos todo2 = Todos.builder()
                    .todoId(2L)
                    .todo("공부하기")
                    .status(Status.READY)
                    .orderNo(2)
                    .task(task)
                    .build();
            private List<TodoUpdateOrder> todoUpdateOrders = List.of(
                    new TodoUpdateOrder(todo1.getTodoId(), 2),
                    new TodoUpdateOrder(todo2.getTodoId(), 1)
            );

            @BeforeEach
            void prepare() {
                given(todoListService.getTodosByIdIn(List.of(todo1.getTodoId(), todo2.getTodoId())))
                        .willReturn(List.of(todo1, todo2));
            }

            @Test
            @DisplayName("순서가 변경된다.")
            void it_return_change_order() {
                List<TodoUpdateResponse> result = todoUpdateService.updateOrder(todoUpdateOrders);

                assertAll(
                        () -> assertThat(result).hasSize(2),
                        () -> assertThat(result.get(0).getTodoId()).isEqualTo(todo1.getTodoId()),
                        () -> assertThat(result.get(0).getOrderNo()).isEqualTo(2),
                        () -> assertThat(result.get(1).getTodoId()).isEqualTo(todo2.getTodoId()),
                        () -> assertThat(result.get(1).getOrderNo()).isEqualTo(1)
                );
            }
        }
    }
}
