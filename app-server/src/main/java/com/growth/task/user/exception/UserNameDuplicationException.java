package com.growth.task.user.exception;

import com.growth.task.commons.error.exception.AlreadyExistsException;

public class UserNameDuplicationException extends AlreadyExistsException {
    public UserNameDuplicationException(String name) {
        super("존재하는 이름입니다.");
    }
}
