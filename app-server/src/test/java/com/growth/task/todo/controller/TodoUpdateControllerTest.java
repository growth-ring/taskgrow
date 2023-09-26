package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.application.TodoService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoStatusUpdateRequest;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
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

@DisplayName("TodoUpdateControllerTest 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class TodoUpdateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TodoService todoService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    private ResultActions performUpdate(Long todoId, Object requestPayload, String apiPath) throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(requestPayload);
        return mockMvc.perform(patch(apiPath, todoId)
                .content(requestBody)
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

    private TodoUpdateRequest getTodoUpdateRequest(String todo, Status status) {
        return TodoUpdateRequest.builder()
                .todo(todo)
                .status(status)
                .build();
    }

    private TodoUpdateRequest getTodoUpdateRequest(String todo) {
        return TodoUpdateRequest.builder()
                .todo(todo)
                .build();
    }

    private TodoUpdateRequest getTodoUpdateRequest(Status status) {
        return TodoUpdateRequest.builder()
                .status(status)
                .build();
    }

    private TodoStatusUpdateRequest getTodoStatusUpdateRequest(Status status) {
        return TodoStatusUpdateRequest.builder().status(status).build();
    }

    @AfterEach
    void cleanUp() {
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("PATCH /api/v1/todos/{todo_id} 요청은")
    class Describe_update {
        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private TodoUpdateRequest todoUpdateRequest;

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-09-26"));
            todo = getTodo(task, "코딩 테스트", Status.READY);
        }

        @Nested
        @DisplayName("todoUpdateRequest 로 todo 와 status 가 들어오는 경우")
        class Context_input_todoUpdateRequest_with_todo_and_status {
            @BeforeEach
            void setUp() {
                todoUpdateRequest = getTodoUpdateRequest("책 읽기", Status.PROGRESS);
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performUpdate(todoId, todoUpdateRequest, "/api/v1/todos/{todo_id}")
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.todo", equalTo("책 읽기")))
                        .andExpect(jsonPath("$.status", equalTo("PROGRESS")));
            }
        }

        @Nested
        @DisplayName("todoUpdateRequest 로 todo 가 들어오는 경우")
        class Context_input_todoUpdateRequest_with_todo {
            @BeforeEach
            void setUp() {
                todoUpdateRequest = getTodoUpdateRequest("음악 듣기");
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performUpdate(todoId, todoUpdateRequest, "/api/v1/todos/{todo_id}")
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.todo", equalTo("음악 듣기")))
                        .andExpect(jsonPath("$.status", equalTo("READY")));
            }
        }

        @Nested
        @DisplayName("todoUpdateRequest 로 status 가 들어오는 경우")
        class Context_input_todoUpdateRequest_with_status {
            @BeforeEach
            void setUp() {
                todoUpdateRequest = getTodoUpdateRequest(Status.DONE);
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performUpdate(todoId, todoUpdateRequest, "/api/v1/todos/{todo_id}")
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.todo", equalTo("코딩 테스트")))
                        .andExpect(jsonPath("$.status", equalTo("DONE")));
            }
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/todos/{todo_id}/status 요청은")
    class Describe_changeStatus {
        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private TodoStatusUpdateRequest todoStatusUpdateRequest;

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-09-26"));
            todo = getTodo(task, "코딩 테스트", Status.READY);
        }

        @Nested
        @DisplayName("TodoStatusUpdateRequest 로 status 값이 PROGRESS 가 들어오는 경우")
        class Context_input_TodoStatusUpdateRequest_with_progress {
            @BeforeEach
            void setUp() {
                todoStatusUpdateRequest = getTodoStatusUpdateRequest(Status.PROGRESS);
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performUpdate(todoId, todoStatusUpdateRequest, "/api/v1/todos/{todo_id}/status")
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.todo", equalTo("코딩 테스트")))
                        .andExpect(jsonPath("$.status", equalTo(Status.PROGRESS.name())));
            }
        }

        @Nested
        @DisplayName("TodoStatusUpdateRequest 로 status 값이 DONE 이 들어오는 경우")
        class Context_input_TodoStatusUpdateRequest_with_done {
            @BeforeEach
            void setUp() {
                todoStatusUpdateRequest = getTodoStatusUpdateRequest(Status.DONE);
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("성공 응답을 반환한다.")
            void it_returns_success_response() throws Exception {
                performUpdate(todoId, todoStatusUpdateRequest, "/api/v1/todos/{todo_id}/status")
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.todo", equalTo("코딩 테스트")))
                        .andExpect(jsonPath("$.status", equalTo(Status.DONE.name())));
            }
        }

        @Nested
        @DisplayName("TodoStatusUpdateRequest 로 status 값이 INGRESS 가 들어오는 경우")
        class Context_input_TodoStatusUpdateRequest_with_ingress {
            String invalidRequestBody;

            @BeforeEach
            void setUp() {
                invalidRequestBody = "{\"status\":\"INGRESS\"}";
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("실패 응답을 반환한다.")
            void it_returns_bad_request_response() throws Exception {
                mockMvc.perform(patch("/api/v1/todos/{todo_id}/status", todoId)
                                .content(invalidRequestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
