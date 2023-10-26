package com.growth.task.review.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.service.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
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

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewAddController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ReviewAddControllerTest {
    public static final String CONTENT = "회고를 작성하였다";
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
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
        reviewRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
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
                        .taskDate(taskDate)
                        .user(user)
                        .build()
        );
    }

    @DisplayName("Review 생성 POST 요청은")
    @Nested
    class Describe_POST {
        private ResultActions subject(ReviewAddRequest request) throws Exception {
            return mockMvc.perform(post("/api/v1/review")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );
        }

        @Nested
        @DisplayName("존재하는 task id와 회고와 기분점수가 주어지면")
        class Context_with_review_request_and_task_id {
            private ReviewAddRequest request;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                request = new ReviewAddRequest(task.getTaskId(), CONTENT, 7);
            }

            @Test
            @DisplayName("회고를 생성하고 201을 응답한다")
            void it_response_201() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("review_id").isNotEmpty())
                ;
            }
        }

        @Nested
        @DisplayName("범위에 어긋나는 기분점수가 주어지면")
        class Context_with_out_bound_feeling_score {
            private ReviewAddRequest request;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                request = new ReviewAddRequest(task.getTaskId(), CONTENT, 0);
            }

            @Test
            @DisplayName("400을 응답한다")
            void it_response_201() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }
        @Nested
        @DisplayName("이미 회고가 있는데 또 생성하면")
        class Context_with_exist_review_task_id {
            private ReviewAddRequest request;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                reviewRepository.save(new Review(task, CONTENT, 7));
                request = new ReviewAddRequest(task.getTaskId(), CONTENT, 2);
            }

            @Test
            @DisplayName("409을 응답한다")
            void it_response_409() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isConflict())
                ;
            }
        }
    }
}
