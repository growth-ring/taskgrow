package com.growth.task.task.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("This User is Not Found.");
    }
}
