package com.growth.task.todo.exception;

public class BadInputParameterException extends RuntimeException {
    public BadInputParameterException() {
        super("Input Parameter is Invalid.");
    }
}
