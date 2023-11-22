package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.request.TodoAddRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TodoAddController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TodoAddControllerTest {
    public static final LocalDate LOCAL_DATE = LocalDate.of(2023, 11, 21);
    public static final String TODO = "디자인패턴의 아름다움 스터디";
    public static final int PERFORM_COUNT = 2;
    public static final int PLAN_COUNT = 5;
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        objectMapper.registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @AfterEach
    void cleanUp() {
        pomodorosRepository.deleteAll();
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @DisplayName("Todo 요청 POST 요청은")
    @Nested
    class Describe_POST {
        private ResultActions subject(TodoAndPomodoroAddRequest request) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/todos")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );
        }

        private Users user;

        @BeforeEach
        void setUser() {
            user = usersRepository.save(Users.builder()
                    .name("user")
                    .password("password")
                    .build());
        }

        @DisplayName("존재하는 Task id와 생성 정보가 주어지면")
        @Nested
        class Context_with_exist_task_id_and_request {
            private Tasks task;
            private TodoAndPomodoroAddRequest request;

            @BeforeEach
            void setTask() {
                task = tasksRepository.save(Tasks.builder()
                        .user(user)
                        .taskDate(LOCAL_DATE)
                        .build());

                TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                        .taskId(task.getTaskId())
                        .todo(TODO)
                        .build();

                PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                        .performCount(PERFORM_COUNT)
                        .planCount(PLAN_COUNT)
                        .build();
                request = TodoAndPomodoroAddRequest.builder()
                        .todoAddRequest(todoAddRequest)
                        .pomodoroAddRequest(pomodoroAddRequest)
                        .build();

            }

            @Test
            @DisplayName("201을 응답한다")
            void it_response_201() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("$.todo_id").exists())
                        .andExpect(jsonPath("$.task_id").exists())
                        .andExpect(jsonPath("$.todo").value(TODO))
                        .andExpect(jsonPath("$.status").value(Status.READY.name()))
                        .andExpect(jsonPath("$.perform_count").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$.plan_count").value(PLAN_COUNT));
            }
        }

        @DisplayName("존재하지 않는 Task id와 생성 정보가 주어지면")
        @Nested
        class Context_with_not_exist_task_id_and_request {
            private Tasks task;
            private TodoAndPomodoroAddRequest request;

            @BeforeEach
            void setTask() {
                task = tasksRepository.save(Tasks.builder()
                        .user(user)
                        .taskDate(LOCAL_DATE)
                        .build());

                TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                        .taskId(task.getTaskId())
                        .todo(TODO)
                        .build();

                PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                        .performCount(PERFORM_COUNT)
                        .planCount(PLAN_COUNT)
                        .build();
                request = TodoAndPomodoroAddRequest.builder()
                        .todoAddRequest(todoAddRequest)
                        .pomodoroAddRequest(pomodoroAddRequest)
                        .build();

                tasksRepository.delete(task);
            }

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isNotFound())
                ;
            }
        }
    }
}
