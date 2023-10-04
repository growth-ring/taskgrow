package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.todo.domain.Todos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PomodoroUpdateServiceTest {
    @Mock
    private PomodorosRepository pomodorosRepository;
    private PomodoroUpdateService pomodoroUpdateService;

    private final Long TODO_ID1 = 1L;
    private final int PLANCOUNT1 = 3;
    private final int PLANCOUNT2 = 5;

    @BeforeEach
    void setUp() {
        pomodoroUpdateService = new PomodoroUpdateService(pomodorosRepository);
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
                    .todo(mock(Todos.class))
                    .build();

            @BeforeEach
            void setUp() {
                given(pomodorosRepository.findByTodo_TodoId(TODO_ID1)).willReturn(Optional.of(pomodoros));
            }

            @Test
            @DisplayName("pomodoroUpdateRequest 를 입력 받아 업데이트한다.")
            void It_updateThePomodoro() {
                Pomodoros response = pomodoroUpdateService.update(TODO_ID1, pomodoroUpdateRequest);

                assertThat(response.getPlanCount()).isEqualTo(PLANCOUNT1);
            }
        }
    }
}
