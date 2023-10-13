package com.growth.task.todo.repository;

import com.growth.task.config.TestQueryDslConfig;
import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
@DataJpaTest
class TodosRepositoryTest {
    public static final int LIMIT = 3;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;
    @Autowired
    private PomodorosRepository pomodorosRepository;

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
                        .taskDate(taskDate)
                        .build()
        );
    }

    private void getTodo(Tasks task, String todo, Status status, int performCount, int planCount) {
        Todos givenTodo = todosRepository.save(
                Todos.builder()
                        .task(task)
                        .todo(todo)
                        .status(status)
                        .build()
        );
        pomodorosRepository.save(Pomodoros.builder().todo(givenTodo)
                .performCount(performCount)
                .planCount(planCount)
                .build());
    }

    @AfterEach
    void cleanUp() {
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
            task = getTask(user, LocalDate.parse("2023-08-29"));
        }

        @Nested
        @DisplayName("taskId와 limit 개수가 주어지고")
        class Context_with_task_id_and_limit {
            @BeforeEach
            void setUp() {
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY, 0, 3);
                getTodo(task, "얼고리즘 읽기", Status.READY, 0, 4);
                getTodo(task, "스프링 인 액션 읽기", Status.DONE, 3, 4);
                getTodo(task, "파이브 라인스 오브 코드 읽기", Status.DONE, 3, 3);
                getTodo(task, "구글 엔지니어는 이렇게 일한다 읽기", Status.PROGRESS, 2, 5);
                getTodo(task, "코틀린 함수형 프로그래밍 읽기", Status.PROGRESS, 2, 4);
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
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY, 0, 3);
                getTodo(task, "얼고리즘 읽기", Status.READY, 0, 2);
                getTodo(task, "스프링 인 액션 읽기", Status.DONE, 2, 2);
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
}
