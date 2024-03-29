package com.growth.task.todo.dto.composite;

import com.growth.task.pomodoro.dto.response.PomodoroAddResponse;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.dto.response.TodoAddResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoAndPomodoroAddResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private String status;
    private String category;
    private int performCount;
    private int planCount;
    private int orderNo;

    @Builder
    public TodoAndPomodoroAddResponse(
            Long todoId,
            Long taskId,
            String todo,
            String status,
            String category,
            int performCount,
            int planCount,
            int orderNo
    ) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
        this.category = category;
        this.performCount = performCount;
        this.planCount = planCount;
        this.orderNo = orderNo;
    }

    public TodoAndPomodoroAddResponse(TodoAddResponse todoAddResponse, PomodoroAddResponse pomodoroAddResponse, TodoCategory category) {
        this.todoId = todoAddResponse.getTodoId();
        this.taskId = todoAddResponse.getTaskId();
        this.todo = todoAddResponse.getTodo();
        this.status = todoAddResponse.getStatus().toString(); // Enum을 문자열로 변환
        this.category = category != null ? category.getCategory().getName() : null;
        this.performCount = pomodoroAddResponse.getPerformCount();
        this.planCount = pomodoroAddResponse.getPlanCount();
        this.orderNo = todoAddResponse.getOrderNo();
    }
}
