package com.example.ledger.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ledger.dto.ApiError;
import com.example.ledger.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- App level exceptions ----
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiError> handleAppException(
        AppException ex,
        HttpServletRequest request) {
        HttpStatus status = mapStatus(ex.code());

        return ResponseEntity.status(status).body(
            ApiError.of(ex.code(), ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidBody(
        HttpMessageNotReadableException ex,
        HttpServletRequest request) {
        return ResponseEntity.badRequest()
            .body(ApiError.of(ErrorCode.INVALID_REQUEST, "Malformed JSON or invalid field type",
                request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
            ApiError.of(ErrorCode.VALIDATION_ERROR, message, request.getRequestURI()));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandled(
        AppException ex,
        HttpServletRequest request) {

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(ErrorCode.INTERNAL_ERROR, "Unexpected ErrorCode", request.getRequestURI()));
    };

    private @NonNull HttpStatus mapStatus(ErrorCode code) {
        return switch (code) {
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
