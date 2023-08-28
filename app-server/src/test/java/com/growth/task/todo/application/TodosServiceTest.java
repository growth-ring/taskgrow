package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import com.growth.task.todo.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TodosServiceTest {

    @Mock
    private TodosRepository todosRepository;

    @InjectMocks
    private TodosService todosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class Describe_getTodosByTaskId {

        @Nested
        class Context_withValidTaskId {

            @BeforeEach
            void setUp() {
                Long taskId = 1L;
                List<Todos> mockedTodos = createMockedTodos();
                when(todosRepository.findByTask_TaskId(taskId)).thenReturn(mockedTodos);
            }

            @Test
            void It_shouldReturnTodoGetResponseList() {
                List<TodoGetResponse> result = todosService.getTodosByTaskId(1L);
                verifyTodoGetResponseList(result);
            }
        }
    }

    private List<Todos> createMockedTodos() {
        Todos todo1 = Todos.builder()
                .todoId(1L)
                .todo("디자인패턴의 아름다움 스터디")
                .status(Status.READY)
                .build();

        Todos todo2 = Todos.builder()
                .todoId(2L)
                .todo("프로젝트 진행하기")
                .status(Status.PROGRESS)
                .build();

        return List.of(todo1, todo2);
    }

    private void verifyTodoGetResponseList(List<TodoGetResponse> result) {
        assertThat(result.size()).isEqualTo(2);

        TodoGetResponse firstResponse = result.get(0);
        assertThat(firstResponse.getTodoId()).isEqualTo(1L);
        assertThat(firstResponse.getTodo()).isEqualTo("디자인패턴의 아름다움 스터디");
        assertThat(firstResponse.getStatus()).isEqualTo(Status.READY);

        TodoGetResponse secondResponse = result.get(1);
        assertThat(secondResponse.getTodoId()).isEqualTo(2L);
        assertThat(secondResponse.getTodo()).isEqualTo("프로젝트 진행하기");
        assertThat(secondResponse.getStatus()).isEqualTo(Status.PROGRESS);
    }
}
