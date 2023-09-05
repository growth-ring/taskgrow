package com.growth.task.task.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.repository.TodosRepository;
import com.growth.task.todo.enums.Status;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskListController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskListControllerTest {
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;
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
                        .taskDate(taskDate.atStartOfDay())
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

    @AfterEach
    void cleanUp() {
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/v1/tasks 요청은")
    class Describe_GET {
        private ResultActions subject(TaskListRequest request) throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("userId", String.valueOf(request.getUserId()));
            params.add("startDate", String.valueOf(request.getStartDate()));
            params.add("endDate", String.valueOf(request.getEndDate()));

            return mockMvc.perform(get("/api/v1/tasks")
                    .params(params)
            );
        }

        Users user = getUser("testuser1", "password");
        Tasks task0 = getTask(user, LocalDate.parse("2023-08-28"));
        Tasks task1 = getTask(user, LocalDate.parse("2023-08-29"));
        Tasks task2 = getTask(user, LocalDate.parse("2023-08-30"));
        Tasks task3 = getTask(user, LocalDate.parse("2023-08-31"));
        Todos todoTask1StatusReady = getTodo(task1, "디자인 패턴의 아름다움 읽기", Status.READY);
        Todos todoTask1StatusReady2 = getTodo(task1, "얼고리즘 읽기", Status.READY);
        Todos todoTask1StatusDone1 = getTodo(task1, "스프링 인 액션 읽기", Status.DONE);
        Todos todoTask1StatusDone2 = getTodo(task1, "파이브 라인스 오브 코드 읽기", Status.DONE);
        Todos todoTask1StatusProgress = getTodo(task1, "구글 엔지니어는 이렇게 일한다 읽기", Status.PROGRESS);
        Todos todoTask2StatusReady = getTodo(task2, "견고한 데이터 엔지니어링 읽기", Status.READY);
        Todos todoTask2StatusReady2 = getTodo(task2, "레거시 코드 활용 전략 읽기", Status.READY);
        Todos todoTask2StatusDone = getTodo(task2, "파이썬 코딩의 기술 읽기", Status.DONE);
        Todos todoTask2StatusDone2 = getTodo(task2, "Two Scoops of Django 읽기", Status.DONE);
        Todos todoTask3StatusReady = getTodo(task3, "운동하기", Status.READY);

        @Nested
        @DisplayName("user Id와 start_date, end_date가 넘어오면")
        class Context_with_user_id_and_start_date_and_end_date {
            TaskListRequest request = TaskListRequest.builder()
                    .userId(user.getUserId())
                    .startDate(LocalDate.parse("2023-08-27"))
                    .endDate(LocalDate.parse("2023-08-31"))
                    .build();

            @Test
            @DisplayName("Task 정보와 남은 투두 개수를 응답한다")
            void it_response_200_and_return_task_info_and_remained_todo_count() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.*", hasSize(4)))
                        .andExpect(jsonPath("$[0].task_date").value(equalTo("2023-08-28T00:00:00")))
                        .andExpect(jsonPath("$[0].todos.remain").value(equalTo(0)))
                        .andExpect(jsonPath("$[0].todos.done").value(equalTo(0)))
                        .andExpect(jsonPath("$[1].task_date").value(equalTo("2023-08-29T00:00:00")))
                        .andExpect(jsonPath("$[1].todos.remain").value(equalTo(3)))
                        .andExpect(jsonPath("$[1].todos.done").value(equalTo(2)))
                        .andExpect(jsonPath("$[2].task_date").value(equalTo("2023-08-30T00:00:00")))
                        .andExpect(jsonPath("$[2].todos.remain").value(equalTo(2)))
                        .andExpect(jsonPath("$[2].todos.done").value(equalTo(2)))
                        .andExpect(jsonPath("$[3].task_date").value(equalTo("2023-08-31T00:00:00")))
                        .andExpect(jsonPath("$[3].todos.remain").value(equalTo(1)))
                        .andExpect(jsonPath("$[3].todos.done").value(equalTo(0)))
                ;
            }
        }

        @Nested
        @DisplayName("날짜 정보가 안넘어오면")
        class Context_without_date_info {
            TaskListRequest request = TaskListRequest.builder()
                    .userId(user.getUserId())
                    .startDate(null)
                    .endDate(null)
                    .build();

            @Test
            @DisplayName("validation error 메세지와 400을 응답한다")
            void it_response_400() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }
    }
}
