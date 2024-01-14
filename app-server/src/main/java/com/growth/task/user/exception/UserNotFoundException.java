package com.growth.task.user.exception;

import com.growth.task.global.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("This User is Not Found.");
    }

    public UserNotFoundException(String name) {
        super(String.format("This User is Not Found. %s", name));
    }
}
