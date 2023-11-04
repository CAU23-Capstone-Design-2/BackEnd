package com.cau.vostom.util.exception;

import com.cau.vostom.util.api.ResponseCode;

public class TeamException extends BaseException{

        public TeamException(ResponseCode responseCode) {
            super(responseCode);
        }
}
