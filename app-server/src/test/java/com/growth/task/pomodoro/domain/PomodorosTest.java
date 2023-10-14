package com.growth.task.pomodoro.domain;

import com.growth.task.todo.domain.Todos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PomodorosTest {
    @Test
    @DisplayName("Pomodoro 생성")
    void create_pomodoro() {
        Todos todo = mock(Todos.class);
        Pomodoros pomodoro = Pomodoros.builder()
                .pomodoroId(1L)
                .todo(todo)
                .performCount(0)
                .planCount(5)
                .build();

        assertThat(pomodoro).isNotNull();
    }

    @Test
    @DisplayName("plan count가 PLAN_COUNT_LIMIT보다 크면 예외가 발생한다")
    void create_fail_pomodoro() {
        Todos todo = mock(Todos.class);

        assertThrows(IllegalArgumentException.class,
                () -> Pomodoros.builder()
                        .pomodoroId(1L)
                        .todo(todo)
                        .performCount(0)
                        .planCount(21)
                        .build());
    }
}
