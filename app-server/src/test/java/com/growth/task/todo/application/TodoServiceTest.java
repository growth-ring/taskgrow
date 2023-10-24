package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodosRepository todosRepository;
    @Mock
    private TasksRepository tasksRepository;

    private TodoService todoService;

    private final Long TASK_ID1 = 1L;
    private final Long TASK_ID2 = 2L;
    private final String WHAT_TO_DO = "디자인패턴의 아름다움 스터디";
    private final String NEW_WHAT_TO_DO = "쿠버네티스 입문";

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todosRepository, tasksRepository);

    }

    @Nested
    @DisplayName("TodoService 의 signUp 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("taskId 로 task 가 확인되는 경우")
        class Context_whenTaskIdExists {
            private final TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                    .taskId(TASK_ID1)
                    .todo(WHAT_TO_DO)
                    .build();

            private final Tasks tasks = Tasks.builder()
                    .taskId(TASK_ID1)
                    .build();

            @BeforeEach
            void setUp() {
                given(tasksRepository.findById(TASK_ID1)).willReturn(Optional.of(tasks));
            }

            @Test
            @DisplayName("todoAddRequest 가 저장된다.")
            void It_saveTheTodo() {
                todoService.save(todoAddRequest);

                verify(todosRepository).save(any(Todos.class));
            }
        }

        @Nested
        @DisplayName("taskId 로 Tasks 가 확인되지 않는 경우")
        class Context_whenTaskIdDoesNotExist {
            private final TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                    .taskId(TASK_ID2)
                    .todo(WHAT_TO_DO)
                    .build();

            @BeforeEach
            void setUp() {
                when(tasksRepository.findById(TASK_ID2)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("TaskNotFoundException 오류를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> todoService.save(todoAddRequest));
            }
        }
    }

    @Nested
    @DisplayName("TodoService 의 update 메서드는")
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
            void setUp() {
                given(todosRepository.findById(TODO_ID1)).willReturn(Optional.of(todos));
            }

            @Test
            @DisplayName("todo 에 대한 TodoUpdateRequest 가 저장된다.")
            void It_updateTheTodo() {
                Todos todos = todoService.update(TODO_ID1, todoUpdateRequest_todo);
                TodoUpdateResponse response = new TodoUpdateResponse(TODO_ID1, todos);

                assertAll(
                        () -> assertThat(response.getTodo()).isEqualTo(NEW_WHAT_TO_DO),
                        () -> assertThat(response.getStatus()).isEqualTo(Status.PROGRESS)
                );
            }

            @Test
            @DisplayName("status 에 대한 TodoUpdateRequest 가 저장된다.")
            void It_updateTheStatus() {
                Todos todos = todoService.update(TODO_ID1, todoUpdateRequest_status);
                TodoUpdateResponse response = new TodoUpdateResponse(TODO_ID1, todos);

                assertAll(
                        () -> assertThat(response.getTodo()).isEqualTo(WHAT_TO_DO),
                        () -> assertThat(response.getStatus()).isEqualTo(Status.DONE)
                );
            }

            @Test
            @DisplayName("todo 와 status 에 대한 TodoUpdateRequest 가 저장된다.")
            void It_updateTheTodoAndStatus() {
                Todos todos = todoService.update(TODO_ID1, todoUpdateRequest_todo_and_status);
                TodoUpdateResponse response = new TodoUpdateResponse(TODO_ID1, todos);

                assertAll(
                        () -> assertThat(response.getTodo()).isEqualTo(NEW_WHAT_TO_DO),
                        () -> assertThat(response.getStatus()).isEqualTo(Status.DONE)
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
                when(todosRepository.findById(TODO_ID1)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("TodoNotFoundException 오류를 던진다.")
            void It_throws_TodoNotFoundException() {
                assertThrows(TodoNotFoundException.class, () -> todoService.update(TODO_ID1, todoUpdateRequest));
            }
        }
    }
}
