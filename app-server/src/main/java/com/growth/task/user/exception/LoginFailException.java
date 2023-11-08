package com.growth.task.user.exception;

import com.growth.task.commons.error.exception.AuthenticationFailureException;

public class LoginFailException extends AuthenticationFailureException {

    public static final String LOGIN_FAIL_MESSAGE = "로그인에 실패했습니다. 이름 또는 비밀번호를 확인하세요.";

    public LoginFailException() {
        super(LOGIN_FAIL_MESSAGE);
    }
}
