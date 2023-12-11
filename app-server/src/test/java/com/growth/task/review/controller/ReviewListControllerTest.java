package com.growth.task.review.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.repository.ReviewRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewListController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ReviewListControllerTest {
    public static final LocalDate LOCAL_DATE_11_12 = LocalDate.parse("2023-11-12");
    public static final LocalDate LOCAL_DATE_11_13 = LocalDate.parse("2023-11-13");
    public static final LocalDate LOCAL_DATE_11_14 = LocalDate.parse("2023-11-14");
    public static final LocalDate LOCAL_DATE_11_15 = LocalDate.parse("2023-11-15");
    public static final LocalDate LOCAL_DATE_11_16 = LocalDate.parse("2023-11-16");
    public static final LocalDate LOCAL_DATE_11_17 = LocalDate.parse("2023-11-17");
    public static final LocalDate LOCAL_DATE_11_18 = LocalDate.parse("2023-11-18");
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

    private Review getReview(Users user, LocalDate taskDate, String subject, String contents, int feelingsScore) {
        Tasks tasks = tasksRepository.save(
                Tasks.builder()
                        .taskDate(taskDate)
                        .user(user)
                        .build()
        );
        return reviewRepository.save(
                Review.builder()
                        .tasks(tasks)
                        .subject(subject)
                        .contents(contents)
                        .feelingsScore(feelingsScore)
                        .build()
        );
    }

    @DisplayName("Review 통계 조회 GET 요청은")
    @Nested
    class Describe_GET {
        private ResultActions subject(Long userId, ReviewStatsRequest request) throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("start_date", String.valueOf(request.getStartDate()));
            params.add("end_date", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get("/api/v1/review/stats/{userId}", userId)
                    .params(params)
            );
        }

        private Users user = getUser("user1", "password");

        @DisplayName("사용자 아이디와 시간 범위가 주어지면")
        @Nested
        class Context_with_userId_and_time_range {
            @BeforeEach
            void prepare() {
                getReview(user, LOCAL_DATE_11_12, "subject1", "review1", 1);
                getReview(user, LOCAL_DATE_11_13, "subject1", "review2", 3);
                getReview(user, LOCAL_DATE_11_14, "subject1", "review3", 5);
                getReview(user, LOCAL_DATE_11_15, "subject1", "review4", 7);
                getReview(user, LOCAL_DATE_11_16, "subject1", "review5", 9);
                getReview(user, LOCAL_DATE_11_17, "subject1", "review6", 10);
                getReview(user, LOCAL_DATE_11_18, "subject1", "review7", 1);
            }

            @Nested
            @DisplayName("user Id와 start_date, end_date가 넘어오면")
            class Context_with_user_id_and_start_date_and_end_date {
                private ReviewStatsRequest request = new ReviewStatsRequest(LOCAL_DATE_11_12, LOCAL_DATE_11_18);

                @Test
                @DisplayName("리뷰 점수별 개수를 응답한다")
                void it_response_200_and_stats() throws Exception {
                    final ResultActions resultActions = subject(user.getUserId(), request);

                    resultActions.andExpect(status().isOk())
                            .andExpect(jsonPath("$.feelings.1").value(2))
                            .andExpect(jsonPath("$.feelings.2").value(0))
                            .andExpect(jsonPath("$.feelings.3").value(1))
                            .andExpect(jsonPath("$.feelings.4").value(0))
                            .andExpect(jsonPath("$.feelings.5").value(1))
                            .andExpect(jsonPath("$.feelings.6").value(0))
                            .andExpect(jsonPath("$.feelings.7").value(1))
                            .andExpect(jsonPath("$.feelings.8").value(0))
                            .andExpect(jsonPath("$.feelings.9").value(1))
                            .andExpect(jsonPath("$.feelings.10").value(1))
                    ;
                }
            }
        }
    }
}
