package com.growth.task.category.exception;

import com.growth.task.global.error.exception.AlreadyExistsException;

public class CategoryAlreadyExistsException extends AlreadyExistsException {

    private static final String DEFAULT_MESSAGE = "이미 존재하는 카테고리입니다.";

    public CategoryAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}
