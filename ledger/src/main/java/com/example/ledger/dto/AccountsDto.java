package com.example.ledger.dto;

import java.math.BigDecimal;

import com.example.ledger.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountsDto {

    public record CreateAccountRequest(
        @NotBlank(message = "Account number is required") String accountNumer,

        @NotNull(message = "Account type is required") AccountType accountType,

        @NotNull(message = "UserID is required") Long userId

    ) {
    }

    public record AccountBalanceResponse(
        Long accountId,
        String accountNumber,
        BigDecimal balance) {
    }
}
