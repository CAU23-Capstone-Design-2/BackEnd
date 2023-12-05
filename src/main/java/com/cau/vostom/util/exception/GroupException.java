package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ResponseCode;

public class GroupException extends BaseException{

        public GroupException(ResponseCode responseCode) {
            super(responseCode);
        }
}
