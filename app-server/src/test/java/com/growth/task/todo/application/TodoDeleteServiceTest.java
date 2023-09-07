package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoDeleteServiceTest {

    @Mock
    private TodosRepository todosRepository;

    private TodoDeleteService todoDeleteService;

    private final Long todoId = 1L;

    @BeforeEach
    void setUp() {
        todoDeleteService = new TodoDeleteService(todosRepository);
    }

    @Nested
    @DisplayName("deleteByTodoId 메서드는")
    class Describe_deleteByTodoId {

        @Nested
        @DisplayName("TodoId 가 주어지면")
        class Context_whenTodoIdExists {

            private final Todos todos = Todos.builder()
                    .todoId(todoId)
                    .build();

            @BeforeEach
            void setUp() {
                when(todosRepository.findById(todoId)).thenReturn(Optional.of(todos));
            }

            @Test
            @DisplayName("해당 Todo 를 삭제한다.")
            void It_shouldDelete() {
                todoDeleteService.deleteByTodoId(todos.getTodoId());
                verify(todosRepository).delete(todos);
            }
        }

        @Nested
        @DisplayName("taskId 가 주어지지 않는다면")
        class Context_whenTaskIdDoesNotExists {

            @BeforeEach
            void setUp() {
                when(todosRepository.findById(null)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("TodoNotFoundException 오류를 던진다.")
            void It_throws_TodoNotFoundException() {
                assertThrows(TodoNotFoundException.class, () -> {
                    todoDeleteService.deleteByTodoId(null);
                });
            }
        }
    }
}
