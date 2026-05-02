package com.example.ledger.dto;

import com.example.ledger.enums.ErrorCode;

public record ApiError(
    ErrorCode code,
    String message,
    String path) {

    public static ApiError of(ErrorCode code, String message, String path) {
        return new ApiError(code, message, path);
    }
}
