package com.growth.task.review.exception;

import com.growth.task.commons.error.exception.BadRequestException;

public class OutOfBoundsException extends BadRequestException {
    public OutOfBoundsException(String message) {
        super(message);
    }
}
