package com.example.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.ledger.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountsDto {

    public record CreateAccountRequest(
        @NotBlank(message = "Account number is required") String accountNumber,

        @NotNull(message = "Account type is required") AccountType accountType,

        @NotNull(message = "UserID is required") Long userId

    ) {
    }

    public record CreateAccountResponse(
        Long accountId,
        Instant createdAt) {
    }

    public record AccountBalanceResponse(
        Long accountId,
        String accountNumber,
        BigDecimal balance) {
    }

    public record AccountDetailsResponse(
        Long accountId,
        String accountNumber,
        AccountType accountType,
        Instant createdAt,
        String username,
        BigDecimal balance) {
    }
}
