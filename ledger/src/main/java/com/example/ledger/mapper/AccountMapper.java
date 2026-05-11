package com.example.ledger.mapper;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.model.Account;
import com.example.ledger.model.User;

@Component
public class AccountMapper {

    public Account toEntity(AccountsDto.CreateAccountRequest request, User user) {
        if (request == null) {
            return null;
        }

        Account account = new Account(
            request.accountNumer(),
            request.accountType(),
            user,
            Instant.now());
        return account;
    }

    public AccountsDto.AccountDetailsResponse toDetailsResponse(Account account, BigDecimal balance) {
        if (account == null) {
            return null;
        }

        return new AccountsDto.AccountDetailsResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getType(),
            account.getCreatedAt(),
            account.getUser().getName(),
            balance);
    }

    public AccountsDto.AccountBalanceResponse toBalanceResponse(Account account, BigDecimal balance) {
        return new AccountsDto.AccountBalanceResponse(
            account.getId(), account.getAccountNumber(), balance);
    }
}
