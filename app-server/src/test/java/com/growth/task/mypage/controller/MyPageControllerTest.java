package com.growth.task.mypage.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewListRequest;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.repository.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepository;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.todo.enums.Status.DONE;
import static com.growth.task.todo.enums.Status.PROGRESS;
import static com.growth.task.todo.enums.Status.READY;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MyPageController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MyPageControllerTest {
    private static final LocalDate LOCAL_DATE_11_12 = LocalDate.of(2023, 11, 12);
    private static final LocalDate LOCAL_DATE_11_13 = LocalDate.of(2023, 11, 13);
    private static final LocalDate LOCAL_DATE_11_14 = LocalDate.of(2023, 11, 14);
    private static final LocalDate LOCAL_DATE_11_15 = LocalDate.of(2023, 11, 15);
    private static final LocalDate LOCAL_DATE_11_16 = LocalDate.of(2023, 11, 16);
    private static final LocalDate LOCAL_DATE_11_17 = LocalDate.of(2023, 11, 17);
    private static final LocalDate LOCAL_DATE_11_18 = LocalDate.of(2023, 11, 18);
    private static final LocalDate LOCAL_DATE_11_19 = LocalDate.of(2023, 11, 19);
    private static final LocalDate LOCAL_DATE_11_20 = LocalDate.of(2023, 11, 20);
    private static final LocalDate LOCAL_DATE_11_21 = LocalDate.of(2023, 11, 21);
    private static final LocalDate LOCAL_DATE_11_22 = LocalDate.of(2023, 11, 22);
    private static final LocalDate LOCAL_DATE_11_23 = LocalDate.of(2023, 11, 23);
    private static final int PERFORM_COUNT = 2;
    private static final int PLAN_COUNT = 5;
    private static final String MYPAGE_TODO_STATS_URL = "/api/v1/mypage/{user_id}/todos/stats";
    private static final String MYPAGE_TODO_LIST_URL = "/api/v1/mypage/{user_id}/todos";
    private static final String MYPAGE_REVIEW_STATS_URL = "/api/v1/mypage/{user_id}/review/stats";
    private static final String MYPAGE_REVIEW_LIST_URL = "/api/v1/mypage/{user_id}/review";
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ReviewRepository reviewRepository;
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
        reviewRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    private Tasks createTask(Users user, LocalDate localDate) {
        return tasksRepository.save(Tasks.builder()
                .user(user)
                .taskDate(localDate)
                .build());
    }

    private Todos createTodo(Tasks task, String todo, Status status, int perform, int plan, int orderNo) {
        Todos save = todosRepository.save(Todos.builder()
                .task(task)
                .todo(todo)
                .status(status)
                .orderNo(orderNo)
                .build());
        pomodorosRepository.save(Pomodoros.builder()
                .performCount(perform)
                .planCount(plan)
                .todo(save)
                .build());
        return save;
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

    private Users getUser(String name, String password) {
        return usersRepository.save(
                Users.builder()
                        .name(name)
                        .password(password)
                        .role(Role.USER)
                        .build()
        );
    }

    @DisplayName("Todo 통계 Get 요청은")
    @Nested
    class Describe_GET_stats {
        private ResultActions subject(Long userId, TodoStatsRequest request) throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", String.valueOf(request.getStartDate()));
            params.add("endDate", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get(MYPAGE_TODO_STATS_URL, userId)
                    .params(params)
            );
        }

        private Users user;

        @BeforeEach
        void setUser() {
            user = getUser("user", "password");
        }

        @DisplayName("존재하는 task id가 주어지면")
        @Nested
        class Context_with_exist_task_id {
            private Tasks task1;
            private Tasks task2;
            private Tasks task3;
            private Tasks task4;
            private Tasks task5;
            private TodoStatsRequest request;

            @BeforeEach
            void setTask() {
                task1 = createTask(user, LOCAL_DATE_11_19);
                task2 = createTask(user, LOCAL_DATE_11_20);
                task3 = createTask(user, LOCAL_DATE_11_21);
                task4 = createTask(user, LOCAL_DATE_11_22);
                task5 = createTask(user, LOCAL_DATE_11_23);

                createTodo(task1, "책 읽기", READY, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task1, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task2, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task2, "책 읽기", DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task2, "테스트 코드 짜기", DONE, PERFORM_COUNT, PLAN_COUNT, 3);
                createTodo(task3, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task3, "책 읽기", DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task4, "테스트 코드 짜기", DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task4, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task4, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 3);

                createTodo(task5, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task5, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 2);

                request = new TodoStatsRequest(LOCAL_DATE_11_19, LOCAL_DATE_11_22);
            }

            @Test
            @DisplayName("총 투두, 완료한 투두, 진행 중 투두, 미완료한 투두를 리턴한다")
            void it_return_todo_list() throws Exception {
                final ResultActions resultActions = subject(user.getUserId(), request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalCount").value(10))
                        .andExpect(jsonPath("$.doneCount").value(7))
                        .andExpect(jsonPath("$.progressCount").value(2))
                        .andExpect(jsonPath("$.undoneCount").value(3))
                ;
            }
        }
    }

    @DisplayName("Todo Get 요청은")
    @Nested
    class Describe_GET_todo {
        private ResultActions subject(Long userId, TodoListRequest request) throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            if (request.getStatus() != null) {
                params.add("status", request.getStatus().name());
            }
            params.add("startDate", String.valueOf(request.getStartDate()));
            params.add("endDate", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get(MYPAGE_TODO_LIST_URL, userId)
                    .params(params)
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

        @DisplayName("존재하는 user id와 status가 주어지면")
        @Nested
        class Context_with_exist_user_id_and_status {
            private Tasks task1;
            private Tasks task2;
            private Tasks task3;
            private Tasks task4;
            private Tasks task5;
            private TodoListRequest request;

            @BeforeEach
            void setTask() {
                task1 = createTask(user, LOCAL_DATE_11_19);
                task2 = createTask(user, LOCAL_DATE_11_20);
                task3 = createTask(user, LOCAL_DATE_11_21);
                task4 = createTask(user, LOCAL_DATE_11_22);
                task5 = createTask(user, LOCAL_DATE_11_23);

                createTodo(task1, "책 읽기", READY, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task1, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task2, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task2, "책 읽기", DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task2, "테스트 코드 짜기", DONE, PERFORM_COUNT, PLAN_COUNT, 3);
                createTodo(task3, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task3, "책 읽기", DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task4, "테스트 코드 짜기", DONE, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task4, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task4, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 3);
                createTodo(task5, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 1);
                createTodo(task5, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 2);
            }

            @ParameterizedTest
            @CsvSource({
                    "READY, 1",
                    "PROGRESS, 3",
                    "DONE, 8"
            })
            @DisplayName("투두 리스트를 리턴한다")
            void it_return_todo_list(Status status, int resultSize) throws Exception {
                request = new TodoListRequest(status, LOCAL_DATE_11_19, LOCAL_DATE_11_23);

                final ResultActions resultActions = subject(user.getUserId(), request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("content", hasSize(resultSize)))
                ;
            }
        }
    }


    @DisplayName("Review 통계 조회 GET 요청은")
    @Nested
    class Describe_GET_review_stats {
        private ResultActions subject(Long userId, ReviewStatsRequest request) throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", String.valueOf(request.getStartDate()));
            params.add("endDate", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get(MYPAGE_REVIEW_STATS_URL, userId)
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

    @DisplayName("Review 통계 조회 상세 리스트 GET 요청은")
    @Nested
    class Describe_GET_Review_list {
        private ResultActions subject(Long userId, ReviewListRequest request) throws Exception {
            List<Integer> feelingsScores = request.getFeelingsScore();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            if (!CollectionUtils.isEmpty(feelingsScores)) {
                feelingsScores.forEach(score -> params.add("feelingsScore", String.valueOf(score)));
            }
            params.add("startDate", String.valueOf(request.getStartDate()));
            params.add("endDate", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get(MYPAGE_REVIEW_LIST_URL, userId)
                    .params(params)
            );
        }

        private Users user = getUser("user1", "password");

        @DisplayName("사용자 아이디와 기분점수, 시간 범위가 주어지면")
        @Nested
        class Context_with_userId_and_feelings_score_and_time_range {
            @BeforeEach
            void prepare() {
                getReview(user, LOCAL_DATE_11_12, "subject1", "review1", 1);
                getReview(user, LOCAL_DATE_11_13, "subject1", "review2", 3);
                getReview(user, LOCAL_DATE_11_14, "subject1", "review3", 3);
                getReview(user, LOCAL_DATE_11_15, "subject1", "review4", 3);
                getReview(user, LOCAL_DATE_11_16, "subject1", "review5", 4);
                getReview(user, LOCAL_DATE_11_17, "subject1", "review6", 5);
                getReview(user, LOCAL_DATE_11_18, "subject1", "review7", 5);
            }

            @ParameterizedTest
            @CsvSource({
                    "1, 1",
                    "3, 3",
                    "4, 1",
                    "5, 2"
            })
            @DisplayName("리뷰 리스트를 응답한다")
            void it_response_200_and_list(Integer feelingsScore, int resultSize) throws Exception {
                final ReviewListRequest request = new ReviewListRequest(List.of(feelingsScore), LOCAL_DATE_11_12, LOCAL_DATE_11_18);
                final ResultActions resultActions = subject(user.getUserId(), request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("content", hasSize(resultSize)))
                ;
            }

            @DisplayName("기분점수 리스트, 시간 범위가 주어지면")
            @Nested
            class Context_with_feelings_scores {
                @Test
                @DisplayName("리뷰 리스트를 응답한다")
                void it_response_200_and_list() throws Exception {
                    final ReviewListRequest request = new ReviewListRequest(List.of(3, 5), LOCAL_DATE_11_12, LOCAL_DATE_11_18);
                    final ResultActions resultActions = subject(user.getUserId(), request);

                    resultActions.andExpect(status().isOk())
                            .andExpect(jsonPath("content", hasSize(5)))
                    ;
                }
            }
        }
    }
}
