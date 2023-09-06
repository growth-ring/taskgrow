package com.growth.task.task.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("This User is Not Found.");
    }

    public UserNotFoundException(String name) {
        super(String.format("This User is Not Found. %s", name));
    }
}
