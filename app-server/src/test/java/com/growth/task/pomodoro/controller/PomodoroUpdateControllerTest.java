package com.growth.task.pomodoro.controller;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.service.PomodoroService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("PomodoroUpdateController 테스트")
@SpringBootTest
@AutoConfigureMockMvc
public class PomodoroUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PomodoroService pomodoroService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    private ResultActions performComplete(Long todoId) throws Exception {
        return mockMvc.perform(patch("/api/v1/pomodoros/{todo_id}/complete", todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private Users getUser(String name, String password) {
        return usersRepository.save(
                Users.builder()
                        .name(name)
                        .password(password)
                        .build()
        );
    }

    private Tasks getTask(Users user, LocalDate taskDate) {
        return tasksRepository.save(
                Tasks.builder()
                        .user(user)
                        .taskDate(taskDate)
                        .build()
        );
    }

    private Todos getTodo(Tasks task, String todo, Status status) {
        return todosRepository.save(
                Todos.builder()
                        .task(task)
                        .todo(todo)
                        .status(status)
                        .build()
        );
    }

    private Pomodoros getPomodoros(Todos todo, int performCount, int planCount) {
        return pomodorosRepository.save(
                Pomodoros.builder()
                        .todo(todo)
                        .performCount(performCount)
                        .planCount(planCount)
                        .build()
        );
    }

    @AfterEach
    void cleanUp() {
        pomodorosRepository.deleteAll();
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("PATCH /api/v1/pomodoros/{todo_id}/complete 요청은")
    class Describe_complete {
        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private Pomodoros pomodoros;

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-09-26"));
            todo = getTodo(task, "코딩 테스트", Status.READY);
        }

        @Nested
        @DisplayName("performCount 가 planCount 보다 작은 경우")
        class Context_with_lower_performCount_than_planCount {
            @BeforeEach
            void setUp() {
                pomodoros = getPomodoros(todo, 0, 3);
                pomodoroService.complete(pomodoros.getTodo().getTodoId());
                todoId = pomodoros.getTodo().getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performComplete(todoId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.perform_count", equalTo(2)))
                        .andExpect(jsonPath("$.plan_count", equalTo(3)));
            }
        }

        @Nested
        @DisplayName("performCount 와 planCount 가 동일한 경우")
        class Context_with_same_performCount_and_planCount {
            @BeforeEach
            void setUp() {
                pomodoros = getPomodoros(todo, 3, 3);
                todoId = pomodoros.getTodo().getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performComplete(todoId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.perform_count", equalTo(4)))
                        .andExpect(jsonPath("$.plan_count", equalTo(3)));
            }
        }

        @Nested
        @DisplayName("performCount 가 planCount 보다 큰 경우")
        class Context_with_greater_performCount_than_planCount {
            @BeforeEach
            void setUp() {
                pomodoros = getPomodoros(todo, 4, 3);
                todoId = pomodoros.getTodo().getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performComplete(todoId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.perform_count", equalTo(5)))
                        .andExpect(jsonPath("$.plan_count", equalTo(3)));
            }
        }
    }
}
