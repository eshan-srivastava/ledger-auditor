package com.example.ledger.exception;

import com.example.ledger.enums.ErrorCode;

public class AppException extends RuntimeException {
    private final ErrorCode code;

    protected AppException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode code() {
        return this.code;
    }
}
