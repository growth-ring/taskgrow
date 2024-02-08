package com.growth.task.todo.dto.response;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TodoWithPomodoroResponse {
    private Long todoId;
    private Long taskId;
    private String todo;
    private Status status;
    private Integer performCount;
    private Integer planCount;
    private int orderNo;

    public TodoWithPomodoroResponse(Long todoId, Long taskId, String todo, Status status, Integer performCount, Integer planCount, int orderNo) {
        this.todoId = todoId;
        this.taskId = taskId;
        this.todo = todo;
        this.status = status;
        this.performCount = performCount;
        this.planCount = planCount;
        this.orderNo = orderNo;
    }

    public TodoWithPomodoroResponse(Todos todos, Pomodoros pomodoros) {
        this.todoId = todos.getTodoId();
        this.taskId = todos.getTask().getTaskId();
        this.todo = todos.getTodo();
        this.status = todos.getStatus();
        this.performCount = pomodoros.getPerformCount();
        this.planCount = pomodoros.getPlanCount();
        this.orderNo = todos.getOrderNo();
    }
}
