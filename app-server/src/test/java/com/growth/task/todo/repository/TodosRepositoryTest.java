package com.growth.task.todo.repository;

import com.growth.task.category.domain.Category;
import com.growth.task.category.repository.CategoryRepository;
import com.growth.task.config.TestQueryDslConfig;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.domain.type.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.todo.enums.Status.DONE;
import static com.growth.task.todo.enums.Status.READY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
@DataJpaTest
class TodosRepositoryTest {
    private static final int LIMIT = 3;
    private static final LocalDate DATE_2023_11_01 = LocalDate.of(2023, 11, 1);
    private static final LocalDate DATE_2023_11_02 = LocalDate.of(2023, 11, 2);
    private static final LocalDate DATE_2023_11_03 = LocalDate.of(2023, 11, 3);
    private static final LocalDate DATE_2023_11_04 = LocalDate.of(2023, 11, 4);
    private static final LocalDate DATE_2023_11_05 = LocalDate.of(2023, 11, 5);
    private static final String 디자인_패턴의_아름다움_읽기 = "디자인 패턴의 아름다움 읽기";
    private static final String 알고리즘_읽기 = "얼고리즘 읽기";
    private static final String 스프링_인_액션_읽기 = "스프링 인 액션 읽기";
    private static final String CATEGORY_READ = "독서";
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

    private Todos getTodoWithPomo(Tasks task, String todo, Status status, int performCount, int planCount, int orderNo) {
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
        return givenTodo;
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

    private Category getCategory(String name) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .build()
        );
    }

    private TodoCategory registTodoCategory(Todos todo, Category category) {
        return todoCategoryRepository.save(
                new TodoCategory(todo, category)
        );
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

    @Nested
    @DisplayName("getTaskTodoPreview")
    class Describe_getTaskTodoPreview {
        private Tasks task;

        @BeforeEach
        void set() {
            Users user = getUser("grow", "password");
            getCategory(CATEGORY_READ);
            task = getTask(user, LocalDate.parse("2023-08-29"));
        }

        @Nested
        @DisplayName("taskId와 limit 개수가 주어지고")
        class Context_with_task_id_and_limit {
            @BeforeEach
            void setUp() {
                getTodoWithPomo(task, "디자인 패턴의 아름다움 읽기", READY, 0, 3, 1);
                getTodoWithPomo(task, "얼고리즘 읽기", READY, 0, 4, 2);
                getTodoWithPomo(task, "스프링 인 액션 읽기", Status.DONE, 3, 4, 3);
                getTodoWithPomo(task, "파이브 라인스 오브 코드 읽기", Status.DONE, 3, 3, 4);
                getTodoWithPomo(task, "구엔이일 읽기", Status.PROGRESS, 2, 5, 5);
                getTodoWithPomo(task, "코틀린 함수형 프로그래밍 읽기", Status.PROGRESS, 2, 4, 6);
            }

            @Test
            @DisplayName("status가 done이 아닌 todo를 limit만큼 가져온다")
            void it_return_limit_when_status_not_done() {
                List<TaskTodoDetailResponse> result = todosRepository.getTaskTodoPreview(task.getTaskId(), LIMIT);

                assertAll(
                        () -> assertThat(result).hasSize(3)
                );
            }
        }

        @Nested
        @DisplayName("Done이 아닌 todo 리스트가 limit보다 적으면")
        class Context_todo_3 {
            @BeforeEach
            void setUp() {
                getTodoWithPomo(task, "디자인 패턴의 아름다움 읽기", READY, 0, 3, 1);
                getTodoWithPomo(task, "얼고리즘 읽기", READY, 0, 2, 2);
                getTodoWithPomo(task, "스프링 인 액션 읽기", Status.DONE, 2, 2, 3);
            }

            @Test
            @DisplayName("다 가져온다")
            void it_return_3() {
                List<TaskTodoDetailResponse> result = todosRepository.getTaskTodoPreview(task.getTaskId(), LIMIT);
                assertThat(result).hasSize(2);
            }
        }

        @Nested
        @DisplayName("todo 리스트가 없다면")
        class Context_with_not_exist {
            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_return_1() {
                List<TaskTodoDetailResponse> result = todosRepository.getTaskTodoPreview(task.getTaskId(), LIMIT);
                assertThat(result).isEmpty();
            }
        }
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


            getTodoWithPomo(task1, "디자인 패턴의 아름다움 읽기", READY, 0, 3, 1);
            getTodoWithPomo(task1, "얼고리즘 읽기", READY, 0, 4, 2);
            getTodoWithPomo(task2, "스프링 인 액션 읽기", Status.DONE, 3, 4, 1);
            getTodoWithPomo(task3, "파이브 라인스 오브 코드 읽기", Status.DONE, 3, 3, 1);
            getTodoWithPomo(task3, "구엔이일 읽기", Status.PROGRESS, 2, 5, 2);
            getTodoWithPomo(task3, "코틀린 함수형 프로그래밍 읽기", Status.PROGRESS, 2, 4, 3);
            getTodoWithPomo(task4, "디자인 패턴의 아름다움 읽기", READY, 0, 3, 1);
            getTodoWithPomo(task4, "얼고리즘 읽기", READY, 0, 4, 2);
            getTodoWithPomo(task4, "스프링 인 액션 읽기", Status.DONE, 3, 4, 3);
            getTodoWithPomo(task5, "파이브 라인스 오브 코드 읽기", Status.DONE, 3, 3, 1);
        }

        @Nested
        @DisplayName("사용자 아이디와 날짜가 주어지면")
        class Context_with_user_id {
            @Test
            @DisplayName("사용자 아이디에 해당하는 todo 리스트를 불러온다")
            void it_return_todo_list_by_user() {
                List<TodoResponse> actual = todosRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), null, null);

                assertThat(actual).hasSize(10);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 끝 날짜가 주어지면")
        class Context_with_user_id_and_end_date {
            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 todo 리스트를 불러온다")
            void it_return_todo_list_by_user() {
                List<TodoResponse> actual = todosRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), null, DATE_2023_11_03);

                assertThat(actual).hasSize(6);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 시작 날짜가 주어지면")
        class Context_with_user_id_and_start_date {
            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 todo 리스트를 불러온다")
            void it_return_todo_list_by_user() {
                List<TodoResponse> actual = todosRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), DATE_2023_11_03, null);

                assertThat(actual).hasSize(7);
            }
        }

        @Nested
        @DisplayName("사용자 아이디와 날짜가 주어지면")
        class Context_with_user_id_and_date {
            @Test
            @DisplayName("사용자 아이디와 날짜 범위에 해당하는 todo 리스트를 불러온다")
            void it_return_todo_list_by_user() {
                List<TodoResponse> actual = todosRepository.findByUserIdAndBetweenTimeRange(user.getUserId(), DATE_2023_11_03, DATE_2023_11_04);

                assertThat(actual).hasSize(6);
            }
        }
    }

    @Nested
    @DisplayName("findTodoWithPomodoroByTaskId")
    class Describe_findTodoWithPomodoroByTaskId {
        private Tasks task;
        private Category category;

        @BeforeEach
        void set() {
            Users user = getUser("grow", "password");
            category = getCategory(CATEGORY_READ);
            task = getTask(user, LocalDate.parse("2023-08-29"));
        }

        @Nested
        @DisplayName("taskId가 주어지면")
        class Context_with_task_id_and_limit {
            @BeforeEach
            void setUp() {
                Todos todo1 = getTodoWithPomo(task, 디자인_패턴의_아름다움_읽기, READY, 0, 3, 1);
                Todos todo2 = getTodoWithPomo(task, 알고리즘_읽기, READY, 0, 4, 2);
                Todos todo3 = getTodoWithPomo(task, 스프링_인_액션_읽기, DONE, 3, 4, 3);
                registTodoCategory(todo1, category);
                registTodoCategory(todo2, category);
                registTodoCategory(todo3, category);
            }

            @Test
            @DisplayName("task id에 해당한느 todo 와 뽀모도로 리스트를 가져온다")
            void it_return_limit_when_status_not_done() {
                List<TodoWithPomodoroResponse> actual = todosRepository.findTodoWithPomodoroByTaskId(task.getTaskId());

                assertAll(
                        () -> assertThat(actual).hasSize(3),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getTodo)
                                .contains(디자인_패턴의_아름다움_읽기, 알고리즘_읽기, 스프링_인_액션_읽기),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getStatus)
                                .contains(READY, READY, DONE),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getPerformCount)
                                .contains(0, 0, 3),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getPlanCount)
                                .contains(3, 4, 4),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getCategory)
                                .contains(CATEGORY_READ, CATEGORY_READ, CATEGORY_READ)
                );
            }
        }

        @Nested
        @DisplayName("taskId가 주어지고, 카테고리에 속한 투두와 속하지 않은 투두가 있는 경우")
        class Context_with_task_id_and_limit_when_category {
            @BeforeEach
            void setUp() {
                Todos todo1 = getTodoWithPomo(task, 디자인_패턴의_아름다움_읽기, READY, 0, 3, 1);
                Todos todo2 = getTodoWithPomo(task, 알고리즘_읽기, READY, 0, 4, 2);
                Todos todo3 = getTodoWithPomo(task, 스프링_인_액션_읽기, DONE, 3, 4, 3);
                registTodoCategory(todo1, category);
            }

            @Test
            @DisplayName("task id에 해당하는 todo 와 뽀모도로 리스트를 가져온다")
            void it_return_limit_when_status_not_done() {
                List<TodoWithPomodoroResponse> actual = todosRepository.findTodoWithPomodoroByTaskId(task.getTaskId());

                assertAll(
                        () -> assertThat(actual).hasSize(3),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getTodo)
                                .contains(디자인_패턴의_아름다움_읽기, 알고리즘_읽기, 스프링_인_액션_읽기),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getStatus)
                                .contains(READY, READY, DONE),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getPerformCount)
                                .contains(0, 0, 3),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getPlanCount)
                                .contains(3, 4, 4),
                        () -> assertThat(actual).extracting(TodoWithPomodoroResponse::getCategory)
                                .contains(CATEGORY_READ, null, null),
                        () -> assertThat(actual.get(0).getCategory()).isEqualTo(CATEGORY_READ),
                        () -> assertThat(actual.get(1).getCategory()).isNull(),
                        () -> assertThat(actual.get(2).getCategory()).isNull()
                );
            }
        }
    }
}
