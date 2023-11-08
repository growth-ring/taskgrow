package com.growth.task.todo.exception;

import com.growth.task.commons.advice.EntityNotFoundException;

public class TodoNotFoundException extends EntityNotFoundException {
    public TodoNotFoundException() {
        super("This Todo is Not Found.");
    }

    public TodoNotFoundException(Long todoId) {
        super(String.format("This Todo is Not Found. %s", todoId));
    }
}
