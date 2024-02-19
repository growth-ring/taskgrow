package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.category.domain.Category;
import com.growth.task.category.repository.CategoryRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodoCategoryRepository;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.domain.type.Role;
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
    public static final String CATEGORY_STUDY = "스터디";
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TodoCategoryRepository todoCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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
        todoCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
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
                    .role(Role.USER)
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
                        .orderNo(1)
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
                        .andExpect(jsonPath("$.todoId").exists())
                        .andExpect(jsonPath("$.taskId").exists())
                        .andExpect(jsonPath("$.todo").value(TODO))
                        .andExpect(jsonPath("$.status").value(Status.READY.name()))
                        .andExpect(jsonPath("$.performCount").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$.planCount").value(PLAN_COUNT))
                        .andExpect(jsonPath("$.orderNo").value(1))
                        .andExpect(jsonPath("$.category").doesNotExist())
                ;
            }
        }

        @DisplayName("존재하는 Task id와 생성 정보, 카테고리가 주어지면")
        @Nested
        class Context_with_exist_task_id_and_request_and_category {
            private Tasks task;
            private TodoAndPomodoroAddRequest request;

            @BeforeEach
            void setTask() {
                task = tasksRepository.save(Tasks.builder()
                        .user(user)
                        .taskDate(LOCAL_DATE)
                        .build());
                Category category = categoryRepository.save(
                        Category.builder()
                                .name(CATEGORY_STUDY)
                                .build()
                );

                TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                        .taskId(task.getTaskId())
                        .todo(TODO)
                        .orderNo(1)
                        .categoryId(category.getId())
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
                        .andExpect(jsonPath("$.todoId").exists())
                        .andExpect(jsonPath("$.taskId").exists())
                        .andExpect(jsonPath("$.todo").value(TODO))
                        .andExpect(jsonPath("$.status").value(Status.READY.name()))
                        .andExpect(jsonPath("$.performCount").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$.planCount").value(PLAN_COUNT))
                        .andExpect(jsonPath("$.orderNo").value(1))
                        .andExpect(jsonPath("$.category").value(CATEGORY_STUDY))
                ;
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
                        .orderNo(1)
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
