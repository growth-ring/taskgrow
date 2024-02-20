package com.growth.task.todo.controller;

import com.growth.task.category.domain.Category;
import com.growth.task.category.repository.CategoryRepository;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.application.TodoDeleteService;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodoCategoryRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TodoDeleteController 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class TodoDeleteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TodoDeleteService todoDeleteService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TodoCategoryRepository todoCategoryRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
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

    private Pomodoros getPomodoros(Todos todo, int performCount, int planCount) {
        return pomodorosRepository.save(
                Pomodoros.builder()
                        .todo(todo)
                        .performCount(performCount)
                        .planCount(planCount)
                        .build()
        );
    }

    private Category createCategory(String name) {
        return categoryRepository.save(Category.builder().name(name).build());
    }

    private TodoCategory addTodoCategory(Todos todo, Category category) {
        return todoCategoryRepository.save(new TodoCategory(todo, category));
    }

    @AfterEach
    void cleanUp() {
        pomodorosRepository.deleteAll();
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("DELETE /api/v1/todos/{todo_id} 요청은")
    class Describe_delete {
        private ResultActions performDelete(Long todoId) throws Exception {
            return mockMvc.perform(delete("/api/v1/todos/{todo_id}", todoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        }

        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private Pomodoros pomodoros;

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-09-26"));
            todo = getTodo(task, "코딩 테스트", Status.READY);
            pomodoros = getPomodoros(todo, 0, 3);
        }

        @Nested
        @DisplayName("유효한 todo_id 가 존재하는 경우")
        class Context_exists_valid_todoId {
            @BeforeEach
            void setUp() {
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("204 상태 코드와 함께 Pomodoro 가 삭제된다.")
            void it_returns_no_content_and_delete_response() throws Exception {
                performDelete(todoId)
                        .andExpect(status().isNoContent());
                assertNull(pomodorosRepository.findById(pomodoros.getPomodoroId()).orElse(null));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/todos/{todo_id}/category 요청은")
    class Describe_delete_category {
        private Long todoId;
        private Users user;
        private Tasks task;
        private Todos todo;
        private Pomodoros pomodoros;

        private ResultActions subject(Long todoId) throws Exception {
            return mockMvc.perform(delete("/api/v1/todos/{todo_id}/category", todoId))
                    ;
        }

        @BeforeEach
        void setUp() {
            user = getUser("user", "password");
            task = getTask(user, LocalDate.parse("2023-09-26"));
            todo = getTodo(task, "코딩 테스트", Status.READY);
            pomodoros = getPomodoros(todo, 0, 3);
            Category category = createCategory("공부");
            addTodoCategory(todo, category);
        }

        @Nested
        @DisplayName("유효한 todo_id 가 존재하는 경우")
        class Context_exists_valid_todoId {
            @BeforeEach
            void setUp() {
                todoId = todo.getTodoId();
            }

            @Test
            @DisplayName("204 상태 코드와 함께 todo의 카테고리가 삭제된다.")
            void it_response_no_content_and_delete_category() throws Exception {
                final ResultActions resultActions = subject(todoId);
                resultActions.andExpect(status().isNoContent());

                assertNull(todoCategoryRepository.findByTodos(todo).orElse(null));
            }
        }
    }
}
