package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.mapper.AccountMapper;
import com.example.ledger.model.Account;
import com.example.ledger.service.AccountService;
import com.example.ledger.service.TransactionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping("/api/accounts")
public class AccountsController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;

    public AccountsController(
        AccountService accountSvc,
        AccountMapper accountMapper,
        TransactionService transactionService) {
        this.accountService = accountSvc;
        this.accountMapper = accountMapper;
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<AccountsDto.CreateAccountResponse> createAccount(
        @RequestBody AccountsDto.CreateAccountRequest entity) {
        AccountsDto.CreateAccountResponse response = accountService.createAccount(entity);
        URI location = URI.create("/api/accounts/" + response.accountId());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{accNum}")
    public ResponseEntity<AccountsDto.AccountDetailsResponse> getAccountDetails(
        @PathVariable("accNum") String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);

        BigDecimal balance = transactionService.getBalanceForAccount(account.getId());

        AccountsDto.AccountDetailsResponse response = accountMapper.toDetailsResponse(account, balance);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accNum}/balance")
    public ResponseEntity<AccountsDto.AccountBalanceResponse> getAccountBalance(
        @PathVariable("accNum") String accountNumber, @RequestParam String param) {

        Account account = accountService.getAccountByNumber(accountNumber);

        BigDecimal balance = transactionService.getBalanceForAccount(account.getId());

        AccountsDto.AccountBalanceResponse response = accountMapper.toBalanceResponse(account, balance);

        return ResponseEntity.ok(response);
    }

}
