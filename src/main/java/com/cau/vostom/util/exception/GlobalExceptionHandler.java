package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ApiResponse<Void> handleUserException(UserException e) {
        log.info("UserException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }

    @ExceptionHandler(GroupException.class)
    public ApiResponse<Void> handleTeamException(GroupException e) {
        log.info("TeamException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }

    @ExceptionHandler(MusicException.class)
    public ApiResponse<Void> handleMusicException(MusicException e) {
        log.info("MusicException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }
}
