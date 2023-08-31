package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import com.growth.task.todo.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TodosServiceTest {

    @Mock
    private TodosRepository todosRepository;

    @Mock
    private PomodorosRepository pomodorosRepository;

    @InjectMocks
    private TodosService todosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("getTodosByTaskId")
    @Nested
    class Describe_getTodosByTaskId {

        @Nested
        class Context_withValidTaskId {

            @BeforeEach
            void setUp() {
                Tasks tasks = Tasks.builder()
                        .taskId(1L)
                        .build();

                Todos todo1 = Todos.builder()
                        .todoId(1L)
                        .task(tasks)
                        .todo("디자인패턴의 아름다움 스터디")
                        .status(Status.READY)
                        .build();

                Todos todo2 = Todos.builder()
                        .todoId(2L)
                        .task(tasks)
                        .todo("프로젝트 진행하기")
                        .status(Status.PROGRESS)
                        .build();

                Pomodoros pomodoro1 = Pomodoros.builder()
                        .pomodoroId(1L)
                        .todo(todo1)
                        .performCount(0)
                        .planCount(1)
                        .build();

                Pomodoros pomodoro2 = Pomodoros.builder()
                        .pomodoroId(2L)
                        .todo(todo2)
                        .performCount(1)
                        .planCount(2)
                        .build();

                List<Todos> todosList = Arrays.asList(todo1, todo2);

                when(todosRepository.findByTask_TaskId(anyLong())).thenReturn(todosList);
                when(pomodorosRepository.findByTodo_TodoId(1L)).thenReturn(Optional.of(pomodoro1));
                when(pomodorosRepository.findByTodo_TodoId(2L)).thenReturn(Optional.of(pomodoro2));
            }

            @Test
            void It_shouldReturnTodoGetResponseList() {
                Long taskId = 1L;
                List<TodoGetResponse> result = todosService.getTodosByTaskId(1L);
                verifyTodoGetResponseList(result, taskId);
            }
        }
    }

    private void verifyTodoGetResponseList(List<TodoGetResponse> result, Long taskId) {
        assertThat(result.size()).isEqualTo(2);

        TodoGetResponse firstResponse = result.get(0);
        assertThat(firstResponse.getTodoId()).isEqualTo(1L);
        assertThat(firstResponse.getTaskId()).isEqualTo(taskId);
        assertThat(firstResponse.getTodo()).isEqualTo("디자인패턴의 아름다움 스터디");
        assertThat(firstResponse.getStatus()).isEqualTo(Status.READY);
        assertThat(firstResponse.getPerformCount()).isEqualTo(0);
        assertThat(firstResponse.getPlanCount()).isEqualTo(1);

        TodoGetResponse secondResponse = result.get(1);
        assertThat(secondResponse.getTodoId()).isEqualTo(2L);
        assertThat(secondResponse.getTaskId()).isEqualTo(taskId);
        assertThat(secondResponse.getTodo()).isEqualTo("프로젝트 진행하기");
        assertThat(secondResponse.getStatus()).isEqualTo(Status.PROGRESS);
        assertThat(secondResponse.getPerformCount()).isEqualTo(1);
        assertThat(secondResponse.getPlanCount()).isEqualTo(2);
    }
}
