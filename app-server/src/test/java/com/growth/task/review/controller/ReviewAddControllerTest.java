package com.growth.task.review.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.repository.ReviewRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static com.growth.task.review.exception.InvalidReviewRequestException.DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewAddController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ReviewAddControllerTest {
    private static final String CONTENT = "회고를 작성하였다";
    private static final LocalDate TASK_DATE_10_26 = LocalDate.of(2023, 10, 26);
    private static final String SUBJECT = "오늘의 한 줄";
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;
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

    private Todos getTodo(String todo, Tasks task) {
        return todosRepository.save(
                Todos.builder()
                        .status(Status.READY)
                        .todo(todo)
                        .task(task)
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
        @DisplayName("Todo가 존재하는 task id와 회고와 기분점수가 주어지면")
        class Context_with_review_request_and_task_id_when_exist_todo {
            private ReviewAddRequest request;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                getTodo("책 읽기", task);

                request = new ReviewAddRequest(task.getTaskId(), SUBJECT, CONTENT, 7);
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
        @DisplayName("Todo가 존재하지 않는 task id와 회고와 기분점수가 주어지면")
        class Context_with_review_request_and_task_id_when_not_exist_todo {
            private ReviewAddRequest request;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, TASK_DATE_10_26);

                request = new ReviewAddRequest(task.getTaskId(), SUBJECT, CONTENT, 7);
            }

            @Test
            @DisplayName("400을 응답한다")
            void it_response_400() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("reason").value(containsString(String.format(DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE, TASK_DATE_10_26))))
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
                request = new ReviewAddRequest(task.getTaskId(), SUBJECT, CONTENT, 0);
            }

            @Test
            @DisplayName("400을 응답한다")
            void it_response_201() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("errors[0].reason").value("기분 점수는 1 ~ 10 사이를 입력해주세요."))
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
                reviewRepository.save(new Review(task, SUBJECT, CONTENT, 7));
                request = new ReviewAddRequest(task.getTaskId(), SUBJECT, CONTENT, 2);
            }

            @Test
            @DisplayName("400을 응답한다")
            void it_response_400() throws Exception {
                ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }
    }
}
