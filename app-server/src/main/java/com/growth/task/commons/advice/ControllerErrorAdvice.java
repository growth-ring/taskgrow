package com.growth.task.commons.advice;

import com.growth.task.task.exception.UserAndTaskDateUniqueConstraintViolationException;
import com.growth.task.todo.exception.BadInputParameterException;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.user.exception.UserNameDuplicationException;
import com.growth.task.user.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class ControllerErrorAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException exception) {
        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, BAD_REQUEST);
    }

    /**
     * User를 찾을 수 없는 경우, NOT_FOUND(404)와 Error 메세지를 응답한다.
     *
     * @param exception User를 찾을 수 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException exception) {
        log.error("UserNotFoundException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, NOT_FOUND);
    }

    /**
     * Input 이 없는 경우, BAD_REQUEST(400) 와 Error 메세지를 응답한다.
     *
     * @param exception Input 이 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadInputParameterException.class)
    public ResponseEntity<Map<String, String>> handleBadInputParameterException(BadInputParameterException exception) {
        log.error("BadInputParameterException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, BAD_REQUEST);
    }

    /**
     * Task 를 찾을 수 없는 경우, NOT_FOUND(404) 와 Error 메세지를 응답한다.
     *
     * @param exception Tasks 를 찾을 수 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskNotFoundException(TaskNotFoundException exception) {
        log.error("TaskNotFoundException: taskId={}", exception.getTaskId());

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, NOT_FOUND);
    }

    /**
     * TodoId 가 없는 경우, NOT_FOUND(404) 와 Error 메세지를 응답한다.
     *
     * @param exception TodoId 를 찾을 수 없다는 예외
     * @return 에러 메세지
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTodoNotFoundException(TodoNotFoundException exception) {
        log.error("TodoNotFoundException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, NOT_FOUND);
    }

    /**
     * 사용자 이름이 이미 있는 경우, CONFLICT(409)와 Error 메세지를 응답한다.
     */
    @ResponseStatus(CONFLICT)
    @ExceptionHandler(UserNameDuplicationException.class)
    public ResponseEntity<Map<String, String>> handleUserNameDuplicationException(UserNameDuplicationException exception) {
        log.error("UserNameDuplicationException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, CONFLICT);
    }

    /**
     * 사용자의 이미 존재하는 테스크 날짜에 요청이 온 경우
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UserAndTaskDateUniqueConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleUserAndTaskDateUniqueConstraintViolationException(UserAndTaskDateUniqueConstraintViolationException exception) {
        log.error("UserAndTaskDateUniqueConstraintViolationException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, BAD_REQUEST);
    }

    /**
     * javax.validation.Valid or @Validated으로 binding error 발생 시 발생한다.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException", exception);

        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        }
        return errors;
    }

    private static Map<String, String> getErrorResponseBody(Exception exception) {
        return Map.of("error", exception.getMessage());
    }
}
