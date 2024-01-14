package com.growth.task.review.repository;

import com.growth.task.config.TestQueryDslConfig;
import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewDetailWithTaskDateResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
@DataJpaTest
class ReviewRepositoryTest {
    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.by("id").descending());
    private static final LocalDate DATE_2023_11_01 = LocalDate.of(2023, 11, 1);
    private static final LocalDate DATE_2023_11_02 = LocalDate.of(2023, 11, 2);
    private static final LocalDate DATE_2023_11_03 = LocalDate.of(2023, 11, 3);
    private static final LocalDate DATE_2023_11_04 = LocalDate.of(2023, 11, 4);
    private static final LocalDate DATE_2023_11_05 = LocalDate.of(2023, 11, 5);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private ReviewRepository reviewRepository;

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
                        .user(user)
                        .taskDate(taskDate)
                        .build()
        );
    }

    private Review getReview(Tasks tasks, String subject, String contents, int feelingsScore) {
        return reviewRepository.save(
                Review.builder()
                        .tasks(tasks)
                        .subject(subject)
                        .contents(contents)
                        .feelingsScore(feelingsScore)
                        .build()
        );
    }

    @Nested
    @DisplayName("findByUserIdAndBetweenTimeRange")
    class Describe_findByUserIdAndBetweenTimeRange {
        private Users user;

        @BeforeEach
        void prepare() {
            user = getUser("test", "1234");
            Tasks task1 = getTask(user, DATE_2023_11_01);
            Tasks task2 = getTask(user, DATE_2023_11_02);
            Tasks task3 = getTask(user, DATE_2023_11_03);
            Tasks task4 = getTask(user, DATE_2023_11_04);
            Tasks task5 = getTask(user, DATE_2023_11_05);

            getReview(task1, "subject 1", "review 1", 1);
            getReview(task2, "subject 1", "review 2", 3);
            getReview(task3, "subject 1", "review 3", 5);
            getReview(task4, "subject 1", "review 4", 7);
            getReview(task5, "subject 1", "review 5", 9);
        }

        @Nested
        @DisplayName("사용자 아이디와 빈 날짜가 주어지면")
        class Context_with_user_id {
            private final ReviewStatsRequest request = new ReviewStatsRequest(null, null);

            @Test
            @DisplayName("사용자 아이디에 해당하는 review 리스트를 불러온다")
            void it_return_review_list_by_user() {
                List<ReviewDetailResponse> actual = reviewRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), request);

                assertThat(actual).hasSize(5);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 시작 날짜가 주어지면")
        class Context_with_user_id_and_start_date {
            private final ReviewStatsRequest request = new ReviewStatsRequest(DATE_2023_11_03, null);

            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 review 리스트를 불러온다")
            void it_return_review_list_by_user_and_between_time_range() {
                List<ReviewDetailResponse> actual = reviewRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), request);

                assertThat(actual).hasSize(3);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 끝 날짜가 주어지면")
        class Context_with_user_id_and_end_date {
            private final ReviewStatsRequest request = new ReviewStatsRequest(null, DATE_2023_11_03);

            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 review 리스트를 불러온다")
            void it_return_review_list_by_user_and_between_time_range() {
                List<ReviewDetailResponse> actual = reviewRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), request);

                assertThat(actual).hasSize(3);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 날짜 범위가 주어지면")
        class Context_with_user_id_and_date_range {
            private final ReviewStatsRequest request = new ReviewStatsRequest(DATE_2023_11_02, DATE_2023_11_04);

            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 review 리스트를 불러온다")
            void it_return_review_list_by_user_and_between_time_range() {
                List<ReviewDetailResponse> actual = reviewRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), request);

                assertThat(actual).hasSize(3);
            }
        }
    }

    @Nested
    @DisplayName("findByUserAndParams")
    class Describe_findByUserAndParams {
        private Users user;

        @BeforeEach
        void prepare() {
            user = getUser("test", "1234");
            Tasks task1 = getTask(user, DATE_2023_11_01);
            Tasks task2 = getTask(user, DATE_2023_11_02);
            Tasks task3 = getTask(user, DATE_2023_11_03);
            Tasks task4 = getTask(user, DATE_2023_11_04);
            Tasks task5 = getTask(user, DATE_2023_11_05);

            getReview(task1, "subject 1", "review 1", 9);
            getReview(task2, "subject 2", "review 2", 9);
            getReview(task3, "subject 3", "review 3", 5);
            getReview(task4, "subject 4", "review 4", 5);
            getReview(task5, "subject 5", "review 5", 1);
        }

        @Nested
        @DisplayName("사용자 아이디와 기분점수, 날짜가 주어지면")
        class Context_with_user_id_feelings_score_task_date {

            @ParameterizedTest(name = "feelings Score = {0}, result size = {1}")
            @CsvSource({
                    "1,1",
                    "5,2",
                    "9,2"
            })
            @DisplayName("기분 점수에 따른 리뷰 리스트를 불러온다")
            void it_return_review_list_by_user(int feelingsScore, int size) {
                Page<ReviewDetailWithTaskDateResponse> actual = reviewRepository.findByUserAndParams(DEFAULT_PAGEABLE, user.getUserId(), List.of(feelingsScore), DATE_2023_11_01, DATE_2023_11_05);

                assertThat(actual).hasSize(size);
            }
        }
    }
}
