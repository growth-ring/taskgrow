package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.category.domain.Category;
import com.growth.task.category.repository.CategoryRepository;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoUpdateOrder;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.growth.task.todo.enums.Status.PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TodoUpdateController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TodoUpdateControllerTest {
    private static final LocalDate LOCAL_DATE_2_9 = LocalDate.of(2023, 11, 19);
    private static final int PERFORM_COUNT = 2;
    private static final int PLAN_COUNT = 5;
    private static final String API_TODOS_ORDER = "/api/v1/todos/order";
    private static final String API_TODOS_ID = "/api/v1/todos/{id}";
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TodoCategoryRepository todoCategoryRepository;

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
        todoCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        pomodorosRepository.deleteAll();
        todosRepository.deleteAll();
        tasksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    private Tasks createTask(Users user, LocalDate localDate) {
        return tasksRepository.saveAndFlush(Tasks.builder()
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

    private Category createCategory(String name) {
        return categoryRepository.save(Category.builder().name(name).build());
    }

    private TodoCategory addTodoCategory(Todos todo, Category category) {
        return todoCategoryRepository.save(new TodoCategory(todo, category));
    }

    @DisplayName("Todo 수정 요청")
    @Nested
    class Describe_PATCH {
        private ResultActions subject(Long todoId, TodoUpdateRequest request) throws Exception {
            return mockMvc.perform(patch(API_TODOS_ID, todoId)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
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

        @DisplayName("Todo 수정 객체가 넘어오면")
        @Nested
        class Context_with_todoUpdate {
            private TodoUpdateRequest request;
            private Tasks task = createTask(user, LOCAL_DATE_2_9);
            private Todos todo = createTodo(task, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 1);

            @BeforeEach
            void prepare() {
                Category category = createCategory("독서");

                request = TodoUpdateRequest.builder()
                        .categoryId(category.getId())
                        .build();
            }

            @Test
            @DisplayName("순서를 수정후 응답한다")
            void it_response_200() throws Exception {
                final ResultActions resultActions = subject(todo.getTodoId(), request);

                resultActions.andExpect(status().isOk())
                ;

                TodoCategory todoCategory = todoCategoryRepository.findByTodos(todo).orElse(null);

                assertAll(
                        () -> assertThat(todoCategory).isNotNull(),
                        () -> assertThat(todoCategory.getCategory().getName()).isEqualTo("독서"),
                        () -> assertThat(todoCategory.getTodos().getTodo()).isEqualTo(todo.getTodo())
                );
            }
        }
    }

    @DisplayName("Todo 순서 수정 요청")
    @Nested
    class Describe_PATCH_Order {
        private ResultActions subject(List<TodoUpdateOrder> requests) throws Exception {
            return mockMvc.perform(patch(API_TODOS_ORDER)
                    .content(objectMapper.writeValueAsString(requests))
                    .contentType(MediaType.APPLICATION_JSON)
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

        @DisplayName("Todo 순서 수정 객체가 넘어오면")
        @Nested
        class Context_with_todoUpdateOrder {
            private List<TodoUpdateOrder> request = new ArrayList<>();
            private Todos todo1;
            private Todos todo2;

            @BeforeEach
            void prepare() {
                Tasks task = createTask(user, LOCAL_DATE_2_9);

                todo1 = createTodo(task, "책 읽기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 1);
                todo2 = createTodo(task, "테스트 코드 짜기", PROGRESS, PERFORM_COUNT, PLAN_COUNT, 2);
                createTodo(task, "스터디 참여", Status.DONE, PERFORM_COUNT, PLAN_COUNT, 3);

                request.addAll(List.of(
                        new TodoUpdateOrder(todo1.getTodoId(), 2),
                        new TodoUpdateOrder(todo2.getTodoId(), 1)
                ));
            }

            @Test
            @DisplayName("순서를 수정후 응답한다")
            void it_response_200() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].todoId").value(todo1.getTodoId()))
                        .andExpect(jsonPath("$[0].todo").value(todo1.getTodo()))
                        .andExpect(jsonPath("$[0].orderNo").value(2))
                        .andExpect(jsonPath("$[1].todoId").value(todo2.getTodoId()))
                        .andExpect(jsonPath("$[1].todo").value(todo2.getTodo()))
                        .andExpect(jsonPath("$[1].orderNo").value(1))
                ;
            }
        }
    }
}
