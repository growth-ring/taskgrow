package com.growth.task.user.exception;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException() {
        super("로그인에 실패했습니다. 이름 또는 비밀번호를 확인하세요.");
    }
}
