package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.domain.TasksRepository;
import com.growth.task.task.dto.TaskAddRequest;
import com.growth.task.task.dto.TaskAddResponse;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TaskAddServiceTest {
    private TaskAddService taskAddService;
    @Mock
    private TasksRepository tasksRepository;
    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        taskAddService = new TaskAddService(tasksRepository, usersRepository);
    }

    @Nested
    @DisplayName("save")
    class Describe_save {
        @Nested
        @DisplayName("Task 생성 정보가 주어지면")
        class Context_wiht_task_info {
            String taskDate = "2023-08-22T10:10:30";
            private final Users givenUser = Users.builder()
                    .userId(1L)
                    .name("test")
                    .password("password")
                    .build();
            private final Tasks givenTask = Tasks.builder()
                    .taskId(1L)
                    .taskDate(LocalDateTime.parse(taskDate))
                    .user(givenUser)
                    .build();
            private final TaskAddRequest taskAddRequest = TaskAddRequest.builder()
                    .userId(givenUser.getUserId())
                    .taskDate(givenTask.getTaskDate())
                    .build();

            @BeforeEach
            void prepare() {
                given(usersRepository.findById(1L))
                        .willReturn(Optional.ofNullable(givenUser));
                given(tasksRepository.save(any()))
                        .willReturn(givenTask);
            }

            @Test
            @DisplayName("테스트를 생성하고 응답객체를 리턴한다")
            void it_return_task_add_response() {
                TaskAddResponse response = taskAddService.save(taskAddRequest);

                assertAll(
                        () -> assertThat(response.getTaskDate()).isEqualTo(taskDate),
                        () -> assertThat(response.getTaskId()).isEqualTo(1L),
                        () -> assertThat(response.getUserId()).isEqualTo(1L)
                );
            }
        }
    }
}
