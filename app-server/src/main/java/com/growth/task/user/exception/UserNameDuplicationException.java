package com.growth.task.user.exception;

public class UserNameDuplicationException extends RuntimeException {
    public UserNameDuplicationException(String name) {
        super("존재하는 이름입니다.");
    }
}
