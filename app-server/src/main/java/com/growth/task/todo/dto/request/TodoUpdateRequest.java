package com.growth.task.todo.dto.request;

import com.growth.task.todo.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 투두 업데이트 요청
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoUpdateRequest {
    private String todo;
    private Status status;

    private Long categoryId;

    @Builder
    public TodoUpdateRequest(String todo, Status status, Long categoryId) {
        this.todo = todo;
        this.status = status;
        this.categoryId = categoryId;
    }

    public boolean hasStatus() {
        return this.status != null;
    }

    public boolean hasTodo() {
        return this.todo != null;
    }

    public boolean hasCategory() {
        return this.categoryId != null;
    }
}
