package com.growth.task.commons.advice;

import com.growth.task.task.exception.UserNotFoundException;
import com.growth.task.todo.exception.BadInputParameterException;
import com.growth.task.todo.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerErrorAdvice {
    /**
     * User를 찾을 수 없는 경우, NOT_FOUND(404)와 Error 메세지를 응답한다.
     *
     * @param exception User를 찾을 수 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException exception) {
        log.error("UserNotFoundException", exception);
        return exception.getMessage();
    }

    /**
     * Input 이 없는 경우, BAD_REQUEST(400) 와 Error 메세지를 응답한다.
     *
     * @param exception Input 이 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadInputParameterException.class)
    public String handleBadInputParameterException(BadInputParameterException exception) {
        log.error("BadInputParameterException", exception);
        return exception.getMessage();
    }

    /**
     * Task 를 찾을 수 없는 경우, NOT_FOUND(404) 와 Error 메세지를 응답한다.
     *
     * @param exception Tasks 를 찾을 수 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public String handleTaskNotFoundException(TaskNotFoundException exception) {
        log.error("TaskNotFoundException", exception);
        return exception.getMessage();
    }
}


