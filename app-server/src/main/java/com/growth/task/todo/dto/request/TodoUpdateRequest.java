package com.growth.task.todo.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.enums.Status;
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
    private String todo;
    private Status status;

    @Builder
    public TodoUpdateRequest(String todo, Status status) {
        this.todo = todo;
        this.status = status;
    }

    public boolean hasStatus() {
        return this.status != null;
    }

    public boolean hasTodo() {
        return this.todo != null;
    }
}
