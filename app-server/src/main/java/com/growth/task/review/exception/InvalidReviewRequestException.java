package com.growth.task.review.exception;

import java.time.LocalDate;

public class InvalidReviewRequestException extends IllegalArgumentException {

    public static final String DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE = "계획된 Todo가 없으면 회고를 작성할 수 없습니다. %s";

    public InvalidReviewRequestException(LocalDate taskDate) {
        super(String.format(DO_NOT_SAVE_REVIEW_WHEN_NOT_EXIST_TODO_EXCEPTION_MESSAGE, taskDate.toString()));
    }
}
