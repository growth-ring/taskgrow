package com.growth.task.review.exception;

import com.growth.task.commons.error.exception.BadArgumentException;

public class OutOfBoundsException extends BadArgumentException {
    public OutOfBoundsException(String message) {
        super(message);
    }
}
