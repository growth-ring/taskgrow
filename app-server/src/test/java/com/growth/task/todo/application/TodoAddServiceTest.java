package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoAddServiceTest {

    @Mock
    private TodosRepository todosRepository;
    @Mock
    private TaskDetailService taskDetailService;

    private TodoAddService todoAddService;

    private final Long TASK_ID1 = 1L;
    private final Long TASK_ID2 = 2L;
    private final String WHAT_TO_DO = "디자인패턴의 아름다움 스터디";

    @BeforeEach
    void setUp() {
        todoAddService = new TodoAddService(todosRepository, taskDetailService);

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
                given(taskDetailService.findTaskById(TASK_ID1)).willReturn(tasks);
            }

            @Test
            @DisplayName("todoAddRequest 가 저장된다.")
            void It_saveTheTodo() {
                todoAddService.save(todoAddRequest);

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
                when(taskDetailService.findTaskById(TASK_ID2)).thenThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("TaskNotFoundException 오류를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> todoAddService.save(todoAddRequest));
            }
        }
    }

}
