package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ResponseCode;

public class MusicException extends BaseException {
    public MusicException(ResponseCode responseCode) {
        super(responseCode);
    }
}
