package com.example.ledger.exception;

import com.example.ledger.enums.ErrorCode;

public class HttpException extends AppException {
    protected HttpException(ErrorCode code, String message) {
        super(code, message);
    }

    public static final class InternalError extends HttpException {
        public InternalError(String message) {
            super(ErrorCode.INTERNAL_ERROR, message);
        }
    }
}
