package com.growth.task.commons.error.exception;

import com.growth.task.commons.error.ErrorCode;

public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, ErrorCode.ALREADY_EXISTS_ERROR);
    }
}
