package com.example.ledger.exception;

import com.example.ledger.enums.ErrorCode;

public class AccountException extends AppException {

    protected AccountException(ErrorCode code, String message) {
        super(code, message);
    }

    public static final class AlreadyExists extends AppException {
        public AlreadyExists(String accountNumber) {
            super(ErrorCode.ACCOUNT_ALREADY_EXISTS, "Account already exists for: " + accountNumber);
        }
    }

    public static final class NotFound extends AppException {
        public NotFound(String acountNumber) {
            super(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found for number: " + acountNumber);
        }
    }
}
