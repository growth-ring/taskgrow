package com.growth.task.todo.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super("This Todo is Not Found.");
    }

    public TodoNotFoundException(Long todoId) {
        super(String.format("This Todo is Not Found. %s", todoId));
    }
}
