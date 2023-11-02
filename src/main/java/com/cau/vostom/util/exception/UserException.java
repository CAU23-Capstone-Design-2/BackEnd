package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ResponseCode;

public class UserException extends BaseException {

    public UserException(ResponseCode responseCode) {
        super(responseCode);
    }
}
