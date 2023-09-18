package com.growth.task.task.exception;

import java.time.LocalDate;

/**
 * 사용자의 이미 존재하는 테스크 날짜에 요청이 온 경우
 */
public class UserAndTaskDateUniqueConstraintViolationException extends RuntimeException {
    public UserAndTaskDateUniqueConstraintViolationException(String name, LocalDate taskDate) {
        super(String.format("Already Exists %s's task for this date( %s ).", name, taskDate));
    }
}
