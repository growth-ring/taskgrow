package com.growth.task.todo.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Todo 업데이트 요청
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoUpdateRequest {
    @NotNull(message = "task 아이디는 필수 입력 값입니다.")
    private Long taskId;
    private String todo;

    @Builder
    public TodoUpdateRequest(Long taskId, String todo) {
        this.taskId = taskId;
        this.todo = todo;
    }

    public Todos toEntity(Tasks tasks) {
        return Todos.builder()
                .task(tasks)
                .build();
    }
}
