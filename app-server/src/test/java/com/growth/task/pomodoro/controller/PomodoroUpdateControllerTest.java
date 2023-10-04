package com.growth.task.pomodoro.controller;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.service.PomodoroAddService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("PomodoroUpdateController")
public class PomodoroUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PomodoroAddService pomodoroAddService;
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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(print()).build();
    }

    private Users getUser(String name, String password) {
        return usersRepository.save(Users.builder().name(name).password(password).build());
    }

    private Tasks getTask(Users user, LocalDate taskDate) {
        return tasksRepository.save(Tasks.builder().user(user).taskDate(taskDate).build());
    }

    private Todos getTodo(Tasks task, String todo, Status status) {
        return todosRepository.save(Todos.builder().task(task).todo(todo).status(status).build());
    }

    private Pomodoros getPomodoros(Todos todo, int performCount, int planCount) {
        return pomodorosRepository.save(Pomodoros.builder().todo(todo).performCount(performCount).planCount(planCount).build());
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
        private ResultActions subject(Long todoId) throws Exception {
            return mockMvc.perform(patch("/api/v1/pomodoros/{todo_id}/complete", todoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        }

        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private Pomodoros pomodoros;

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-10-03"));
            todo = getTodo(task, "스터디", Status.READY);
        }

        @Nested
        @DisplayName("존재하는 todo Id에 해당하는 뽀모도로가 완료되면")
        class Context_with_exist_todoId_when_pomodoro_complete {
            @BeforeEach
            void setUp() {
                pomodoros = getPomodoros(todo, 4, 3);
                todoId = pomodoros.getTodo().getTodoId();
            }

            @Test
            @DisplayName("todo 상태가 Progress로 변경되고 perform_count가 1개 올라가고 200을 응답한다")
            void it_response_200() throws Exception {
                subject(todoId).andExpect(status().isOk())
                        .andExpect(jsonPath("$.perform_count", equalTo(5)))
                        .andExpect(jsonPath("$.plan_count", equalTo(3)));

                Todos todos = todosRepository.findById(todoId).orElse(null);
                assertThat(todos.getStatus()).isEqualTo(Status.PROGRESS);
            }
        }

        @Nested
        @DisplayName("이미 완료된 todo Id에 해당하는 뽀모도로가 완료되면")
        class Context_with_already_done_todoId_when_pomodoro_complete {
            @BeforeEach
            void setUp() {
                Todos doneTodo = getTodo(task, "책 읽기", Status.DONE);
                pomodoros = getPomodoros(doneTodo, 4, 3);
                todoId = doneTodo.getTodoId();
            }

            @Test
            @DisplayName("400을 응답한다")
            void it_response_400() throws Exception {
                subject(todoId).andExpect(status().isBadRequest())
                ;
            }
        }

        @Nested
        @DisplayName("존재하지 않은 todo Id에 해당하는 뽀모도로가 완료되면")
        class Context_with_not_exist_todoId_when_pomodoro_complete {
            @BeforeEach
            void setUp() {
                pomodoros = getPomodoros(todo, 4, 3);
                todoId = pomodoros.getTodo().getTodoId();
                todosRepository.deleteById(todoId);
            }

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                subject(todoId).andExpect(status().isNotFound());
            }
        }
    }
}
