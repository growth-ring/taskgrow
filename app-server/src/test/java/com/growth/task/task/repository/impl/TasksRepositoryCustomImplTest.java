package com.growth.task.task.repository.impl;

import com.growth.task.config.TestQueryDslConfig;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskListQueryResponse;
import com.growth.task.task.dto.TaskListRequest;
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
class TasksRepositoryCustomImplTest {
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

    private void getTodo(Tasks task, String todo, Status status, int orderNo) {
        todosRepository.save(
                Todos.builder()
                        .task(task)
                        .todo(todo)
                        .status(status)
                        .orderNo(orderNo)
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
    @DisplayName("findRemainedTodosByUserBetweenTimeRange")
    class Describe_findRemainedTodosByUserBetweenTimeRange {
        private Users user;
        private Tasks task0;
        private Tasks task1;
        private Tasks task2;
        private Tasks task3;

        @BeforeEach
        void set() {
            user = getUser("test1", "password");
            task0 = getTask(user, LocalDate.parse("2023-08-28"));
            task1 = getTask(user, LocalDate.parse("2023-08-29"));
            task2 = getTask(user, LocalDate.parse("2023-08-30"));
            task3 = getTask(user, LocalDate.parse("2023-08-31"));
        }

        @Nested
        @DisplayName("user id와 task_date 날짜 범위가 주어지면")
        class Context_with_user_id_and_task_date_range {
            private TaskListRequest request;

            @BeforeEach
            void setContext() {
                getTodo(task1, "디자인 패턴의 아름다움 읽기", Status.READY, 1);
                getTodo(task1, "얼고리즘 읽기", Status.READY, 2);
                getTodo(task1, "스프링 인 액션 읽기", Status.DONE, 3);
                getTodo(task1, "파이브 라인스 오브 코드 읽기", Status.DONE, 4);
                getTodo(task1, "구엔이일 읽기", Status.PROGRESS, 5);
                getTodo(task2, "견고한 데이터 엔지니어링 읽기", Status.PROGRESS, 1);
                getTodo(task2, "레거시 코드 활용 전략 읽기", Status.READY, 2);
                getTodo(task2, "파이썬 코딩의 기술 읽기", Status.DONE, 3);
                getTodo(task2, "투스툽 장고 읽기", Status.DONE, 4);
                getTodo(task3, "운동하기", Status.DONE, 1);

                request = TaskListRequest.builder().userId(user.getUserId())
                        .startDate(LocalDate.from(task0.getTaskDate()))
                        .endDate(LocalDate.from(task2.getTaskDate()))
                        .build();
            }

            @Test
            @DisplayName("task 정보와 todo status를 반환한다")
            void it_return_task_info_and_todo_count() {
                List<TaskListQueryResponse> result = tasksRepository.findRemainedTodosByUserBetweenTimeRange(request);

                assertAll(
                        () -> assertThat(result).hasSize(10),
                        () -> assertThat(result)
                                .filteredOn(task -> Status.READY.equals(task.getTodoStatus()))
                                .hasSize(3),
                        () -> assertThat(result)
                                .filteredOn(task -> Status.PROGRESS.equals(task.getTodoStatus()))
                                .hasSize(2),
                        () -> assertThat(result)
                                .filteredOn(task -> Status.DONE.equals(task.getTodoStatus()))
                                .hasSize(4)

                );
            }
        }
    }
}
