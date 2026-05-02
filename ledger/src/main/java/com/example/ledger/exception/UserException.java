package com.example.ledger.exception;

import com.example.ledger.enums.ErrorCode;

public sealed class UserException extends AppException
    permits UserException.NotFound, UserException.AlreadyExists {

    protected UserException(ErrorCode code, String message) {
        super(code, message);
    }

    public static final class NotFound extends UserException {
        public NotFound(Long userId) {
            super(ErrorCode.USER_NOT_FOUND, "User not found: " + userId);
        }
    }

    public static final class AlreadyExists extends UserException {
        public AlreadyExists(String email) {
            super(ErrorCode.USER_ALREADY_EXISTS, "User already exists for this email: " + email);
        }
    }
}