package com.example.ledger.exception;

import com.example.ledger.enums.ErrorCode;

public class TransactionException extends AppException {

    protected TransactionException(ErrorCode code, String message) {
        super(code, message);
    }

    public static final class NotFound extends TransactionException {
        public NotFound(Long transactionId) {
            super(ErrorCode.TXN_NOT_FOUND, "Transaction not found: " + transactionId);
        }
    }

    public static final class AlreadyExists extends TransactionException {
        public AlreadyExists(String txnNumber) {
            super(ErrorCode.TXN_ALREADY_EXISTS, "Transaction already exists: " + txnNumber);
        }
    }
}
