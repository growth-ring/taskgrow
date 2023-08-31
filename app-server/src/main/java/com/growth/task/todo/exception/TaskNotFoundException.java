package com.growth.task.todo.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("This Task is Not Found.");
    }
}
