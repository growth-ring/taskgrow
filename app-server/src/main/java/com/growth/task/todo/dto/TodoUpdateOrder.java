package com.growth.task.todo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TodoUpdateOrder {
    @NotNull(message = "todo id는 필수입니다.")
    private Long todoId;
    @Min(value = 1, message = "1 이상이어야 합니다.")
    private int orderNo;

    public TodoUpdateOrder(Long todoId, int orderNo) {
        this.todoId = todoId;
        this.orderNo = orderNo;
    }
}
