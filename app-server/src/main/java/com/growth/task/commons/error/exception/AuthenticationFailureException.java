package com.growth.task.commons.error.exception;

import com.growth.task.commons.error.ErrorCode;

public class AuthenticationFailureException extends BusinessException {
    public AuthenticationFailureException(String message) {
        super(message, ErrorCode.AUTHENTICATE_FAIL_ERROR);
    }
}
