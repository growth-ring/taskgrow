package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoDetailService")
class TodoDetailServiceTest {
    public static final long TODO_ID = 1L;
    public static final String TODO_TEXT = "테스트 짜기";
    private TodoDetailService todoDetailService;
    @Mock
    private TodosRepository todosRepository;

    @BeforeEach
    void setUp() {
        todoDetailService = new TodoDetailService(todosRepository);
    }

    @Nested
    @DisplayName("getTodo")
    class Describe_getTodo {
        @Nested
        @DisplayName("존재하는 Todo의 아이디가 주어지면")
        class Context_with_todo_id {
            private Todos todo = Todos.builder()
                    .todoId(TODO_ID)
                    .status(Status.READY)
                    .todo(TODO_TEXT)
                    .task(mock(Tasks.class))
                    .build();

            @BeforeEach
            void prepare() {
                given(todosRepository.findById(TODO_ID))
                        .willReturn(Optional.of(todo));
            }

            @Test
            @DisplayName("id에 해당하는 Todo를 리턴한다")
            void it_return_todo() {
                Todos actual = todoDetailService.getTodo(TODO_ID);

                assertAll(
                        () -> assertThat(actual.getTodoId()).isEqualTo(TODO_ID),
                        () -> assertThat(actual.getStatus()).isEqualTo(todo.getStatus()),
                        () -> assertThat(actual.getTodo()).isEqualTo(todo.getTodo())
                );
            }
        }

        @Nested
        @DisplayName("존재하지 않은 Todo의 아이디가 주어지면")
        class Context_with_not_exist_todo_id {
            private Todos todo = Todos.builder()
                    .todoId(TODO_ID)
                    .status(Status.READY)
                    .todo(TODO_TEXT)
                    .task(mock(Tasks.class))
                    .build();

            @BeforeEach
            void prepare() {
                given(todosRepository.findById(TODO_ID))
                        .willThrow(TodoNotFoundException.class);
            }

            @Test
            @DisplayName("id에 해당하는 Todo를 리턴한다")
            void it_return_todo() {
                Executable when = () -> todoDetailService.getTodo(TODO_ID);

                assertThrows(TodoNotFoundException.class, when);
            }
        }
    }
}
