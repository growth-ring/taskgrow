package com.growth.task.task.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskDetailController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskDetailControllerTest {
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;
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

    private void getTodo(Tasks task, String todo, Status status, int performCount, int planCount, int orderNo) {
        Todos givenTodo = todosRepository.save(
                Todos.builder()
                        .task(task)
                        .todo(todo)
                        .status(status)
                        .orderNo(orderNo)
                        .build()
        );
        pomodorosRepository.save(Pomodoros.builder().todo(givenTodo)
                .performCount(performCount)
                .planCount(planCount)
                .build());
    }


    @AfterEach
    void cleanUp() {
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/v1/tasks/{taskId}/todos 요청은")
    class Describe_GET {
        private ResultActions subject(Long taskId) throws Exception {
            return mockMvc.perform(get("/api/v1/tasks/{taskId}/todos", taskId));
        }

        private Users user;
        private Tasks task;

        @BeforeEach
        void setUp() {
            user = getUser("testuser1", "password");
            task = getTask(user, LocalDate.parse("2023-08-29"));
            getTask(user, LocalDate.parse("2023-08-30"));
        }

        @Nested
        @DisplayName("todo를 가진 taskId가 넘어오면")
        class Context_when_taskId_with_todo {
            @BeforeEach
            void setUp() {
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY, 0, 3, 1);
                getTodo(task, "얼고리즘 읽기", Status.READY, 0, 2, 2);
                getTodo(task, "스프링 인 액션 읽기", Status.DONE, 2, 2, 3);
                getTodo(task, "파이브 라인스 오브 코드 읽기", Status.DONE, 3, 3, 4);
                getTodo(task, "구엔이일 읽기", Status.PROGRESS, 1, 3, 5);
                getTodo(task, "코틀린 인 액션 읽기", Status.PROGRESS, 1, 3, 6);
                getTodo(task, "코드 구현하기", Status.DONE, 3, 3, 7);
            }

            @Test
            @DisplayName("Task 정보와 투두와 뽀모도로 개수를 응답한다")
            void it_response_200_and_return_task_todo_and_pomodoro() throws Exception {
                final ResultActions resultActions = subject(task.getTaskId());

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("taskId").value(equalTo(task.getTaskId().intValue())))
                        .andExpect(jsonPath("todos", hasSize(3)))
                ;
            }
        }

        @Nested
        @DisplayName("todo 가지지않은 taskId가 넘어오면")
        class Context_when_taskId_that_does_not_have_todo {
            @Test
            @DisplayName("Task 정보와 빈 배열을 응답한다")
            void it_response_200_and_return_empty_list() throws Exception {
                final ResultActions resultActions = subject(task.getTaskId());

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("taskId").value(equalTo(task.getTaskId().intValue())))
                        .andExpect(jsonPath("todos", hasSize(0)))
                ;
            }
        }

        @Nested
        @DisplayName("존재하지 않은 taskId가 넘어오면")
        class Context_when_not_exist_taskId {
            @Test
            @DisplayName("NOT FOUND(404)를 응답한다")
            void it_response_404() throws Exception {
                final ResultActions resultActions = subject(0L);

                resultActions.andExpect(status().isNotFound())
                ;
            }
        }
    }
}
