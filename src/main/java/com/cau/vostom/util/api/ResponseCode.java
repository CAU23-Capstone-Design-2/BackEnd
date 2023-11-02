package com.cau.vostom.util.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청입니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, false, "권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, false, "사용자를 찾을 수 없습니다."),
    MUSIC_NOT_FOUND(HttpStatus.NOT_FOUND, false, "음악을 찾을 수 없습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, false, "그룹을 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),

    // 409 Conflict
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, false, "이미 존재하는 사용자입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),

    // 200 OK
    USER_LOGINED(HttpStatus.OK, true, "로그인 되었습니다."),
    USER_UPDATED(HttpStatus.OK, true, "사용자 정보가 수정되었습니다."),
    USER_DELETED(HttpStatus.OK, true, "사용자가 삭제되었습니다."),
    USER_VOICE_DATA_UPDATED(HttpStatus.OK, true, "학습 데이터가 수정되었습니다."),
    USER_READ(HttpStatus.OK, true, "사용자 정보를 조회하였습니다."),
    COMMENT_READ(HttpStatus.OK, true, "사용자의 댓글을 조회하였습니다."),
    MUSIC_READ(HttpStatus.OK, true, "사용자의 음악을 조회하였습니다."),
    MUSIC_DELETED(HttpStatus.OK, true, "음악이 삭제되었습니다."),


    // 201 Created
    USER_CREATED(HttpStatus.CREATED, true, "사용자가 생성되었습니다.");


    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
