package com.growth.task.commons.error.exception;

import com.growth.task.commons.error.ErrorCode;
import com.growth.task.commons.error.exception.BusinessException;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_ERROR);
    }
}
