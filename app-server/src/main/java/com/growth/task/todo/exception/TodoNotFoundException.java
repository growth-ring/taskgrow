package com.growth.task.todo.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super("This Todo is Not Found");
    }
}
