package com.growth.task.category.exception;

import com.growth.task.global.error.exception.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE = "카테고리를 찾을 수 없습니다.";

    public CategoryNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
