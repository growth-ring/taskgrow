package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.domain.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodosServiceTest {

    @Mock
    private TodosRepository todosRepository;

    @Mock
    private PomodorosRepository pomodorosRepository;

    @Mock
    private TasksRepository tasksRepository;

    @InjectMocks
    private TodosService todosService;

    private static final Long TASK_ID = 1L;
    private static final Long TODO_ID1 = 1L;
    private static final Long TODO_ID2 = 2L;
    private static final Long POMODORO_ID1 = 1L;
    private static final Long POMODORO_ID2 = 2L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("getTodosByTaskId 메서드는")
    @Nested
    class Describe_getTodosByTaskId {

        @Nested
        @DisplayName("taskId 로 Tasks 가 확인되지 않는 경우")
        class Context_whenTaskIdDoesNotExist {

            @BeforeEach
            void setup() {
                when(tasksRepository.existsById(TASK_ID)).thenReturn(false);
            }

            @Test
            @DisplayName("TaskNotFoundException 오류를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> todosService.getTodosByTaskId(TASK_ID));
            }
        }

        @Nested
        @DisplayName("taskId 가 존재하지만 Todos 가 없는 경우")
        class Context_whenTaskIdExistsButNoTodos {

            @BeforeEach
            void setup() {
                when(tasksRepository.existsById(TASK_ID)).thenReturn(true);
                when(todosRepository.findByTask_TaskId(TASK_ID)).thenReturn(Collections.emptyList());
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void It_returns_emptyList() {
                List<TodoGetResponse> result = todosService.getTodosByTaskId(TASK_ID);
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("유효한 taskId 와 Todos 가 있는 경우")
        class Context_withValidTaskIdAndTodos {

            @BeforeEach
            void setUp() {
                setUpMocks();
            }

            @Test
            @DisplayName("TodoGetResponse 리스트를 반환한다.")
            void It_shouldReturnTodoGetResponseList() {
                List<TodoGetResponse> result = todosService.getTodosByTaskId(TASK_ID);
                verifyTodoGetResponseList(result, TASK_ID);
            }
        }
    }

    private void setUpMocks() {
        Tasks tasks = Tasks.builder()
                .taskId(TASK_ID)
                .build();

        Todos todo1 = Todos.builder()
                .todoId(TODO_ID1)
                .task(tasks)
                .todo("디자인패턴의 아름다움 스터디")
                .status(Status.READY)
                .build();

        Todos todo2 = Todos.builder()
                .todoId(TODO_ID2)
                .task(tasks)
                .todo("프로젝트 진행하기")
                .status(Status.PROGRESS)
                .build();

        Pomodoros pomodoro1 = Pomodoros.builder()
                .pomodoroId(POMODORO_ID1)
                .todo(todo1)
                .performCount(0)
                .planCount(1)
                .build();

        Pomodoros pomodoro2 = Pomodoros.builder()
                .pomodoroId(POMODORO_ID2)
                .todo(todo2)
                .performCount(1)
                .planCount(2)
                .build();

        List<Todos> todosList = Arrays.asList(todo1, todo2);

        when(tasksRepository.existsById(TASK_ID)).thenReturn(true);
        when(todosRepository.findByTask_TaskId(TASK_ID)).thenReturn(todosList);
        when(pomodorosRepository.findByTodo_TodoId(TODO_ID1)).thenReturn(Optional.of(pomodoro1));
        when(pomodorosRepository.findByTodo_TodoId(TODO_ID2)).thenReturn(Optional.of(pomodoro2));
    }

    private void verifyTodoGetResponseList(List<TodoGetResponse> result, Long taskId) {
        assertThat(result.size()).isEqualTo(2);

        TodoGetResponse firstResponse = result.get(0);
        assertThat(firstResponse.getTodoId()).isEqualTo(TODO_ID1);
        assertThat(firstResponse.getTaskId()).isEqualTo(taskId);
        assertThat(firstResponse.getTodo()).isEqualTo("디자인패턴의 아름다움 스터디");
        assertThat(firstResponse.getStatus()).isEqualTo(Status.READY);
        assertThat(firstResponse.getPerformCount()).isEqualTo(0);
        assertThat(firstResponse.getPlanCount()).isEqualTo(1);

        TodoGetResponse secondResponse = result.get(1);
        assertThat(secondResponse.getTodoId()).isEqualTo(TODO_ID2);
        assertThat(secondResponse.getTaskId()).isEqualTo(taskId);
        assertThat(secondResponse.getTodo()).isEqualTo("프로젝트 진행하기");
        assertThat(secondResponse.getStatus()).isEqualTo(Status.PROGRESS);
        assertThat(secondResponse.getPerformCount()).isEqualTo(1);
        assertThat(secondResponse.getPlanCount()).isEqualTo(2);
    }
}
