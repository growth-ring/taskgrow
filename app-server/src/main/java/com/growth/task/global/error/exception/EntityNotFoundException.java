package com.growth.task.global.error.exception;

import com.growth.task.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_ERROR);
    }
}
