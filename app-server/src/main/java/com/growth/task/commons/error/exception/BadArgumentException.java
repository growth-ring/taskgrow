package com.growth.task.commons.error.exception;

import com.growth.task.commons.error.ErrorCode;

public class BadArgumentException extends BusinessException {
    public BadArgumentException(String message) {
        super(message, ErrorCode.BAD_REQUEST_ERROR);
    }
}
