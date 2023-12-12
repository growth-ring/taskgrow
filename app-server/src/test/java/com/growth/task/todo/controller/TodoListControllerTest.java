package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static com.growth.task.todo.enums.Status.DONE;
import static com.growth.task.todo.enums.Status.PROGRESS;
import static com.growth.task.todo.enums.Status.READY;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TodoListController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TodoListControllerTest {
    public static final LocalDate LOCAL_DATE_11_19 = LocalDate.of(2023, 11, 19);
    public static final LocalDate LOCAL_DATE_11_20 = LocalDate.of(2023, 11, 20);
    public static final LocalDate LOCAL_DATE_11_21 = LocalDate.of(2023, 11, 21);
    public static final LocalDate LOCAL_DATE_11_22 = LocalDate.of(2023, 11, 22);
    public static final LocalDate LOCAL_DATE_11_23 = LocalDate.of(2023, 11, 23);
    public static final String TODO = "디자인패턴의 아름다움 스터디";
    public static final int PERFORM_COUNT = 2;
    public static final int PLAN_COUNT = 5;
    public static final int FIVE = 5;
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

    private Tasks createTask(Users user, LocalDate localDate) {
        return tasksRepository.save(Tasks.builder()
                .user(user)
                .taskDate(localDate)
                .build());
    }

    private Todos createTodo(Tasks task, String todo, Status status, int perform, int plan) {
        Todos save = todosRepository.save(Todos.builder()
                .task(task)
                .todo(todo)
                .status(status)
                .build());
        pomodorosRepository.save(Pomodoros.builder()
                .performCount(perform)
                .planCount(plan)
                .todo(save)
                .build());
        return save;
    }

    @DisplayName("Todo 리스트 Get 요청은")
    @Nested
    class Describe_GET {
        private ResultActions subject(Long taskId) throws Exception {
            return mockMvc.perform(get("/api/v1/todos")
                    .param("task_id", String.valueOf(taskId))
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

        @DisplayName("존재하는 task id가 주어지면")
        @Nested
        class Context_with_exist_task_id {
            private Tasks task;
            private TodoAndPomodoroAddRequest request;

            @BeforeEach
            void setTask() {
                task = createTask(user, LOCAL_DATE_11_21);

                createTodo(task, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT);
                createTodo(task, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT);
                createTodo(task, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT);
            }

            @Test
            @DisplayName("task id에 해당하는 todo 리스트를 리턴한다")
            void it_return_todo_list() throws Exception {
                final ResultActions resultActions = subject(task.getTaskId());

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(3)))
                        .andExpect(jsonPath("$[0].todo_id").exists())
                        .andExpect(jsonPath("$[0].task_id").exists())
                        .andExpect(jsonPath("$[0].todo").value("책 읽기"))
                        .andExpect(jsonPath("$[0].status").value(PROGRESS.name()))
                        .andExpect(jsonPath("$[0].perform_count").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$[0].plan_count").value(PLAN_COUNT))
                        .andExpect(jsonPath("$[1].todo_id").exists())
                        .andExpect(jsonPath("$[1].task_id").exists())
                        .andExpect(jsonPath("$[1].todo").value("테스트 코드 짜기"))
                        .andExpect(jsonPath("$[1].status").value(PROGRESS.name()))
                        .andExpect(jsonPath("$[1].perform_count").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$[1].plan_count").value(PLAN_COUNT))
                        .andExpect(jsonPath("$[2].todo_id").exists())
                        .andExpect(jsonPath("$[2].task_id").exists())
                        .andExpect(jsonPath("$[2].todo").value("스터디 참여"))
                        .andExpect(jsonPath("$[2].status").value(DONE.name()))
                        .andExpect(jsonPath("$[2].perform_count").value(PERFORM_COUNT))
                        .andExpect(jsonPath("$[2].plan_count").value(PLAN_COUNT))
                ;

            }
        }

        @DisplayName("존재하지 않는 task id가 주어지면")
        @Nested
        class Context_with_not_exist_task_id {
            private Tasks task;
            private TodoAndPomodoroAddRequest request;

            @BeforeEach
            void setTask() {
                task = createTask(user, LOCAL_DATE_11_21);

                createTodo(task, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT);
                createTodo(task, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT);
                createTodo(task, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT);

                tasksRepository.delete(task);
            }

            @Test
            @DisplayName("빈 리스트를 리턴한다")
            void it_return_empty_list() throws Exception {
                final ResultActions resultActions = subject(task.getTaskId());

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$").isEmpty())
                ;
            }
        }
    }
}
