package com.growth.task.commons.error.exception;

import com.growth.task.commons.error.ErrorCode;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST_ERROR);
    }
}
