package com.growth.task.global.error.exception;

import com.growth.task.global.error.ErrorCode;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST_ERROR);
    }
}
