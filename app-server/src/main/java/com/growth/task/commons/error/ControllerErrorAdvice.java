package com.growth.task.commons.error;

import com.growth.task.commons.error.exception.BusinessException;
import com.growth.task.review.exception.AlreadyReviewException;
import com.growth.task.user.exception.AuthenticationFailureException;
import com.growth.task.user.exception.UserNameDuplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, BAD_REQUEST);
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

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationFailureException(AuthenticationFailureException exception) {
        log.error("AuthenticationFailureException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, UNAUTHORIZED);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(AlreadyReviewException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyReviewException(AlreadyReviewException exception) {
        log.error("AlreadyReviewException", exception);

        Map<String, String> errorResponseBody = getErrorResponseBody(exception);
        return new ResponseEntity<>(errorResponseBody, CONFLICT);
    }

    /**
     * javax.validation.Valid or @Validated으로 binding error 발생 시 발생한다.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException", exception);

        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
        log.error("handleBusinessException", exception);
        final ErrorCode errorCode = exception.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    private static Map<String, String> getErrorResponseBody(Exception exception) {
        return Map.of("error", exception.getMessage());
    }
}
