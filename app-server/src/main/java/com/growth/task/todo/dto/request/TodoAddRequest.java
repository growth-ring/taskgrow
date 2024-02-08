package com.growth.task.todo.dto.request;

import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Todo 생성 요청
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoAddRequest {
    @NotNull(message = "task 아이디는 필수 입력 값입니다.")
    private Long taskId;
    @NotNull(message = "할 일은 필수 입력 값입니다.")
    private String todo;

    @Min(value = 1, message = "1 이상이어야 합니다.")
    private int orderNo;

    @Builder
    public TodoAddRequest(Long taskId, String todo, int orderNo) {
        this.taskId = taskId;
        this.todo = todo;
        this.orderNo = orderNo;
    }

    public Todos toEntity(Tasks tasks) {
        return Todos.builder()
                .task(tasks)
                .todo(this.todo)
                .status(Status.READY)
                .orderNo(orderNo)
                .build();
    }
}
