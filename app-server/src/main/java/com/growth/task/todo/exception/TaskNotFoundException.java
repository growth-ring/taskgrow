package com.growth.task.todo.exception;

import com.growth.task.commons.error.exception.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
    private final Long taskId;

    public TaskNotFoundException(Long taskId) {
        super("This Task is Not Found: taskId=" + taskId);
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}
