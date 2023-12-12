package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {

    private final ResponseCode responseCode;

    @Override
    public String getMessage() {
        return responseCode.getMessage();
    }
}
