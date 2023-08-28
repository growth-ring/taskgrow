package com.growth.task.task.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.task.domain.TasksRepository;
import com.growth.task.task.dto.TaskAddRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskAddController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskAddControllerTest {
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
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        objectMapper.registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private ResultActions subject(TaskAddRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/tasks")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        );
    }

    private Users getUser(String name, String password) {
        return usersRepository.save(
                Users.builder()
                        .name(name)
                        .password(password)
                        .build()
        );
    }

    @AfterEach
    void cleanUp() {
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("Task 생성 POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("존재하는 사용자 id와 Task 생성 요청 정보가 주어지면")
        class Context_with_task_request_and_existed_user {
            private Users givenUser = getUser("test user", "password");
            private String taskDate = "2023-08-22T10:10:30";
            private TaskAddRequest taskAddRequest = TaskAddRequest.builder()
                    .userId(givenUser.getUserId())
                    .taskDate(LocalDateTime.parse(taskDate))
                    .build();

            @DisplayName("Task를 생성하고, 201을 응답한다")
            @Test
            void it_response_201() throws Exception {
                final ResultActions resultActions = subject(taskAddRequest);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("task_date").value(taskDate))
                ;
            }
        }

        @Nested
        @DisplayName("존재하지않은 사용자 id와 Task 생성 요청 정보가 주어지면")
        class Context_with_task_request_and_not_exist_user {
            private Long invalidId = 0L;
            private String taskDate = "2023-08-22T10:10:30";
            private TaskAddRequest taskAddRequest = TaskAddRequest.builder()
                    .userId(invalidId)
                    .taskDate(LocalDateTime.parse(taskDate))
                    .build();

            @DisplayName("404를 응답한다")
            @Test
            void it_response_404() throws Exception {
                final ResultActions resultActions = subject(taskAddRequest);

                resultActions.andExpect(status().isNotFound())
                ;
            }
        }

        @Nested
        @DisplayName("user 정보가 없다면")
        class Context_without_user_in_task_request {
            private String taskDate = "2023-08-22T10:10:30";
            private TaskAddRequest taskAddRequest = TaskAddRequest.builder()
                    .userId(null)
                    .taskDate(LocalDateTime.parse(taskDate))
                    .build();

            @DisplayName("400를 응답한다")
            @Test
            void it_response_400() throws Exception {
                final ResultActions resultActions = subject(taskAddRequest);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }

        @Nested
        @DisplayName("테스크 날짜 정보가 없다면")
        class Context_without_taskDate_in_task_request {
            TaskAddRequest taskAddRequest = TaskAddRequest.builder()
                    .userId(0L)
                    .taskDate(null)
                    .build();

            @DisplayName("400를 응답한다")
            @Test
            void it_response_400() throws Exception {
                final ResultActions resultActions = subject(taskAddRequest);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }
    }
}
