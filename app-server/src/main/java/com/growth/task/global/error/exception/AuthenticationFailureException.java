package com.growth.task.global.error.exception;

import com.growth.task.global.error.ErrorCode;

public class AuthenticationFailureException extends BusinessException {
    public AuthenticationFailureException(String message) {
        super(message, ErrorCode.AUTHENTICATE_FAIL_ERROR);
    }
}
