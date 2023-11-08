package com.growth.task.review.exception;

import com.growth.task.commons.error.exception.BadRequestException;

public class AlreadyReviewException extends BadRequestException {
    public AlreadyReviewException(String message) {
        super(message);
    }
}
