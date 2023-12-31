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
    NOT_LEADER(HttpStatus.FORBIDDEN, false, "팀 리더가 아닙니다."),
    NOT_COMMENT_OWNER(HttpStatus.FORBIDDEN, false, "댓글 작성자가 아닙니다."),
    LEADER_CANNOT_LEAVE(HttpStatus.FORBIDDEN, false, "팀 리더는 팀을 탈퇴할 수 없습니다."),
    NOT_GROUP_MEMBER(HttpStatus.FORBIDDEN, false, "팀 멤버가 아닙니다."),
    NOT_MUSIC_OWNER(HttpStatus.FORBIDDEN, false, "그룹 음악 소유자가 아닙니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, false, "사용자를 찾을 수 없습니다."),
    MUSIC_NOT_FOUND(HttpStatus.NOT_FOUND, false, "음악을 찾을 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, false, "그룹을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, false, "댓글을 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),

    // 409 Conflict
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, false, "이미 존재하는 사용자입니다."),
    GROUP_ALREADY_JOINED(HttpStatus.CONFLICT, false, "이미 가입한 그룹입니다."),
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, false, "이미 좋아요한 음악입니다."),
    LIKE_ALREADY_DELETED(HttpStatus.CONFLICT, false, "이미 좋아요 취소한 음악입니다."),
    MUSIC_ALREADY_EXISTS(HttpStatus.CONFLICT, false, "이미 존재하는 음악입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, "파일 업로드에 실패하였습니다."),

    // 200 OK
    USER_LOGINED(HttpStatus.OK, true, "로그인 되었습니다."),
    USER_UPDATED(HttpStatus.OK, true, "사용자 정보가 수정되었습니다."),
    USER_DELETED(HttpStatus.OK, true, "사용자가 삭제되었습니다."),
    USER_VOICE_DATA_UPDATED(HttpStatus.OK, true, "학습 데이터가 수정되었습니다."),
    USER_READ(HttpStatus.OK, true, "사용자 정보를 조회하였습니다."),
    COMMENT_READ(HttpStatus.OK, true, "사용자의 댓글을 조회하였습니다."),
    MY_MUSIC_READ(HttpStatus.OK, true, "사용자의 음악을 조회하였습니다."),
    GROUP_MUSIC_READ(HttpStatus.OK, true, "그룹의 음악을 조회하였습니다."),
    MUSIC_LIKED_READ(HttpStatus.OK, true, "사용자가 좋아요한 음악을 조회하였습니다."),
    MUSIC_DELETED(HttpStatus.OK, true, "음악이 삭제되었습니다."),
    GROUP_MUSIC_DELETED(HttpStatus.OK, true, "그룹 음악이 삭제되었습니다."),
    MUSIC_UPLOADED(HttpStatus.OK, true, "그룹에 음악이 업로드되었습니다."),
    GROUP_CREATED(HttpStatus.OK, true, "그룹이 생성되었습니다."),
    GROUP_LEAVED(HttpStatus.OK, true, "그룹이 탈퇴되었습니다."),
    GROUP_DELETED(HttpStatus.OK, true, "그룹이 삭제되었습니다."),
    GROUP_UPDATED(HttpStatus.OK, true, "그룹 정보가 수정되었습니다."),
    GROUP_LISTED(HttpStatus.OK, true, "그룹 정보를 조회하였습니다."),
    MY_GROUP_LISTED(HttpStatus.OK, true, "내 그룹 정보를 조회하였습니다."),
    GROUP_DETAIL_READ(HttpStatus.OK, true, "그룹 상세 정보를 조회하였습니다."),
    COMMENT_CREATED(HttpStatus.OK, true, "댓글이 생성되었습니다."),
    CELEBRITY_LISTED(HttpStatus.OK, true, "연예인 리스트를 조회하였습니다."),
    GROUP_JOINED(HttpStatus.OK, true, "그룹에 가입되었습니다."),
    MUSIC_LIKE_CREATED(HttpStatus.OK, true, "노래에 좋아요가 생성되었습니다."),
    MUSIC_LIKE_UNDO(HttpStatus.OK, true, "노래 좋아요가 취소되었습니다."),
    MUSIC_COMMENT_READ(HttpStatus.OK, true, "노래의 댓글을 조회하였습니다."),
    COMMENT_LIKE_CREATED(HttpStatus.OK, true, "댓글 좋아요가 생성되었습니다."),
    COMMENT_LIKE_UNDO(HttpStatus.OK, true, "댓글 좋아요가 취소되었습니다."),
    COMMENT_DELETED(HttpStatus.OK, true, "댓글이 삭제되었습니다."),
    COMMENT_UPDATED(HttpStatus.OK, true, "댓글이 수정되었습니다."),
    MUSIC_TRAIN_REQUESTED(HttpStatus.OK, true, "음악 학습이 요청되었습니다."),
    REQUESTED_MUSIC_READ(HttpStatus.OK, true, "요청중인 음악을 조회하였습니다."),
    USER_VOICE_DATA_UPLOADED(HttpStatus.OK, true, "학습 데이터가 업로드되었습니다."),
    CHECK_TRAINED(HttpStatus.OK, true, "사용자 학습 상태를 조회했습니다."),
    CELEBRITY_MUSIC_LISTED(HttpStatus.OK, true, "연예인의 노래 리스트를 조회하였습니다."),
    SONG_ADDED(HttpStatus.OK, true, "노래가 추가되었습니다."),
    SONG_REMOVED(HttpStatus.OK, true, "노래가 삭제되었습니다."),
    CRAWL_SUCCESS(HttpStatus.OK, true, "크롤링이 완료되었습니다."),
    MUSIC_FOUND(HttpStatus.OK, true, "음악을 조회하였습니다."),
    USER_RETRAINED(HttpStatus.OK, true, "사용자 목소리 재학습 요청되었습니다."),

    // 201 Created
    USER_CREATED(HttpStatus.CREATED, true, "사용자가 생성되었습니다.");


    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
