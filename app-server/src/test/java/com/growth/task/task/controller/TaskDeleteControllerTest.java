package com.growth.task.task.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskDeleteController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskDeleteControllerTest {
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
    @DisplayName("DELETE /api/v1/tasks/{taskId} 요청은")
    class Describe_DELETE {
        private ResultActions subjects(Long taskId) throws Exception {
            return mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId));
        }

        @Nested
        @DisplayName("todo가 없는 Task id가 주어지면")
        class Context_with_exist_task_and_without_todo {
            private Tasks givenTask;

            @BeforeEach
            void setUp() {
                Users givenUser = getUser("test user", "password");
                givenTask = getTask(givenUser, LocalDate.parse("2023-09-03"));
            }

            @Test
            @DisplayName("id에 해당하는 task를 삭제하고, NO_CONTENT(204)를 응답한다")
            void it_response_204() throws Exception {
                final ResultActions resultActions = subjects(givenTask.getTaskId());

                resultActions.andExpect(status().isNoContent())
                ;

                Optional<Tasks> result = tasksRepository.findById(givenTask.getTaskId());
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("todo가 있는 Task id가 주어지면")
        class Context_with_exist_task_and_with_todo {
            private Tasks givenTask;

            @BeforeEach
            void setUp() {
                Users givenUser = getUser("test user", "password");
                givenTask = getTask(givenUser, LocalDate.parse("2023-09-03"));
                getTodo(givenTask, "디자인 패턴의 아름다움 읽기", Status.READY);
            }

            @Test
            @DisplayName("id에 해당하는 task와 todo를 삭제하고, NO_CONTENT(204)를 응답한다")
            void it_response_204() throws Exception {
                Long taskId = givenTask.getTaskId();
                final ResultActions resultActions = subjects(taskId);

                resultActions.andExpect(status().isNoContent())
                ;

                Optional<Tasks> resultTask = tasksRepository.findById(taskId);
                List<Todos> todoByTaskId = todosRepository.findByTask_TaskId(taskId);
                assertAll(
                        () -> assertThat(resultTask).isEmpty(),
                        () -> assertThat(todoByTaskId).isEmpty()
                );
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Task id가 주어지면")
        class Context_with_not_exist_task_id {
            private Long taskId;

            @BeforeEach
            void setUp() {
                Users givenUser = getUser("test user", "password");
                taskId = getTask(givenUser, LocalDate.parse("2023-09-03")).getTaskId();
                tasksRepository.deleteById(taskId);
            }

            @Test
            @DisplayName("(404) 찾을 수 없다고 응답한다")
            void it_response_404() throws Exception {
                final ResultActions resultActions = subjects(taskId);

                resultActions.andExpect(status().isNotFound())
                ;
            }
        }
    }
}
