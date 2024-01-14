package com.growth.task.global.error.exception;

import com.growth.task.global.error.ErrorCode;

public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, ErrorCode.ALREADY_EXISTS_ERROR);
    }
}
