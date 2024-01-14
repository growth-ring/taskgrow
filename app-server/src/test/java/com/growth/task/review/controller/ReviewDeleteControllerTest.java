package com.growth.task.review.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.review.domain.Review;
import com.growth.task.review.repository.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewDeleteController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ReviewDeleteControllerTest {
    public static final String CONTENTS = "테스트를 작성하였다. 기분이 좋다";

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
                        .role(Role.USER)
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

    private Review getReview(Tasks tasks, String contents, int feelingsScore) {
        return reviewRepository.save(
                Review.builder()
                        .tasks(tasks)
                        .subject("오늘의 한 줄")
                        .contents(contents)
                        .feelingsScore(feelingsScore)
                        .build()
        );
    }

    @DisplayName("Review 삭제 요청은")
    @Nested
    class Describe_DELETE {
        private ResultActions subject(Long reviewId) throws Exception {
            return mockMvc.perform(delete("/api/v1/review/{reviewId}", reviewId));
        }

        @Nested
        @DisplayName("존재하는 Review id가 주어지면")
        class Context_with_exist_review_id {
            private Review review;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                review = getReview(task, CONTENTS, 7);
            }

            @Test
            @DisplayName("204을 응답한다")
            void it_response_204() throws Exception {
                ResultActions resultActions = subject(review.getId());
                resultActions.andExpect(status().isNoContent())
                ;
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Review id가 주어지면")
        class Context_with_not_exist_review_id {
            private Review review;

            @BeforeEach
            void prepare() {
                Users user = getUser("grow", "password");
                Tasks task = getTask(user, LocalDate.of(2023, 10, 26));
                review = getReview(task, CONTENTS, 7);
                reviewRepository.deleteById(review.getId());
            }

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                ResultActions resultActions = subject(review.getId());
                resultActions.andExpect(status().isNotFound())
                ;
            }
        }
    }
}
