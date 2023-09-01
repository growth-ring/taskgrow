package com.growth.task.todo.domain;

import com.growth.task.config.TestQueryDslConfig;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
@DataJpaTest
class TodosRepositoryTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private TodosRepository todosRepository;

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

    private void getTodo(Tasks task, String todo, Status status) {
        todosRepository.save(
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
    @DisplayName("findTop3ByTask_TaskId")
    class Describe_findTop3ByTask_TaskId {
        private Tasks task;

        @BeforeEach
        void set() {
            Users user = getUser("grow", "password");
            task = getTask(user, LocalDate.parse("2023-08-29"));
        }

        @Nested
        @DisplayName("todo 리스트가 3개보다 많다면")
        class Context_more_than_3 {
            @BeforeEach
            void setUp() {
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY);
                getTodo(task, "얼고리즘 읽기", Status.READY);
                getTodo(task, "스프링 인 액션 읽기", Status.DONE);
                getTodo(task, "파이브 라인스 오브 코드 읽기", Status.DONE);
                getTodo(task, "구글 엔지니어는 이렇게 일한다 읽기", Status.PROGRESS);
            }

            @Test
            @DisplayName("3개만 가져온다")
            void it_return_3() {
                List<Todos> result = todosRepository.findTop3ByTask_TaskId(task.getTaskId());
                assertThat(result).hasSize(3);
            }
        }

        @Nested
        @DisplayName("todo 리스트가 3개면")
        class Context_todo_3 {
            @BeforeEach
            void setUp() {
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY);
                getTodo(task, "얼고리즘 읽기", Status.READY);
                getTodo(task, "스프링 인 액션 읽기", Status.DONE);
            }

            @Test
            @DisplayName("3개 가져온다")
            void it_return_3() {
                List<Todos> result = todosRepository.findTop3ByTask_TaskId(task.getTaskId());
                assertThat(result).hasSize(3);
            }
        }

        @Nested
        @DisplayName("todo 리스트가 3개보다 적은 1개라면")
        class Context_less_than_3 {
            @BeforeEach
            void setUp() {
                getTodo(task, "디자인 패턴의 아름다움 읽기", Status.READY);
            }

            @Test
            @DisplayName("1개 가져온다")
            void it_return_1() {
                List<Todos> result = todosRepository.findTop3ByTask_TaskId(task.getTaskId());
                assertThat(result).hasSize(1);
            }
        }

        @Nested
        @DisplayName("todo 리스트가 없다면")
        class Context_with_not_exist {
            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_return_1() {
                List<Todos> result = todosRepository.findTop3ByTask_TaskId(task.getTaskId());
                assertThat(result).isEmpty();
            }
        }
    }
}