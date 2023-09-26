package com.growth.task.todo.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.todo.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoStatusUpdateRequest {
    @NotNull(message = "상태 값은 반드시 입력되어야 합니다.")
    private Status status;

    @Builder
    public TodoStatusUpdateRequest(Status status) {
        this.status = status;
    }
}
