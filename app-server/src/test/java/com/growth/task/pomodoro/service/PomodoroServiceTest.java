package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TodoNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PomodoroServiceTest {
    @Mock
    private PomodorosRepository pomodorosRepository;

    private PomodoroService pomodoroService;
    private Todos todos;

    private final Long TODO_ID1 = 1L;
    private final int PLANCOUNT1 = 3;
    private final int PLANCOUNT2 = 5;

    @BeforeEach
    void setUp() {
        pomodoroService = new PomodoroService(pomodorosRepository);
        todos = mock(Todos.class);
    }

    @Nested
    @DisplayName("PomodoroService 의 save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("todoId 가 null 이 아닌 경우")
        class Context_whenTodoIdExists {
            private final PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                    .planCount(PLANCOUNT1)
                    .build();

            private final Todos todos = Todos.builder()
                    .todoId(TODO_ID1)
                    .build();

            @Test
            @DisplayName("todos 를 입력 받아 pomodoros 로 변환하여 저장된다.")
            void It_saveThePomodoro() {
                pomodoroService.save(pomodoroAddRequest, todos);

                verify(pomodorosRepository).save(any(Pomodoros.class));
            }
        }

        @Nested
        @DisplayName("todoId 가 null 인 경우")
        class Context_whenTodoIdDoesNotExist {
            private final PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                    .planCount(PLANCOUNT2)
                    .build();

            @BeforeEach
            void setUp() {
                given(todos.getTodoId()).willReturn(null);
            }

            @Test
            @DisplayName("TaskNotFoundException 오류를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThrows(TodoNotFoundException.class, () -> {
                    pomodoroService.save(pomodoroAddRequest, todos);
                });
            }
        }
    }

    @Nested
    @DisplayName("PomodoroService 의 update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("todoId 가 null 이 아닌 경우")
        class Context_whenTodoIdExists {
            private final PomodoroUpdateRequest pomodoroUpdateRequest = PomodoroUpdateRequest.builder()
                    .planCount(PLANCOUNT1)
                    .build();

            private final Pomodoros pomodoros = Pomodoros.builder()
                    .planCount(PLANCOUNT2)
                    .build();

            @BeforeEach
            void setUp() {
                given(pomodorosRepository.findByTodo_TodoId(TODO_ID1)).willReturn(Optional.of(pomodoros));
            }

            @Test
            @DisplayName("pomodoroUpdateRequest 를 입력 받아 업데이트한다.")
            void It_updateThePomodoro() {
                Pomodoros response = pomodoroService.update(TODO_ID1, pomodoroUpdateRequest);

                assertThat(response.getPlanCount()).isEqualTo(PLANCOUNT1);
            }
        }
    }
}
