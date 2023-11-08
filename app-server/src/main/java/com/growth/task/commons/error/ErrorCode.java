package com.growth.task.commons.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),
    // 요청 값이 이미 존재하는 경우
    ALREADY_EXISTS_ERROR(400, "G002", "Already Exist Exception"),
    // 인증에 실패함
    AUTHENTICATE_FAIL_ERROR(401, "G003", "Authentication Fail Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G004", "Forbidden Exception"),
    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G005", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G006", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(400, "G007", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "G008", "Header에 데이터가 존재하지 않는 경우 "),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
