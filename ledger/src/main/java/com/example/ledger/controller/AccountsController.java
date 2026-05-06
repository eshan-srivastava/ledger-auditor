package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.service.AccountService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    public AccountsController(AccountService accountSvc) {
        this.accountService = accountSvc;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createAccount(@RequestBody AccountsDto.CreateAccountRequest entity) {
        Long accountId = accountService.createAccount(entity);
        URI location = URI.create("/api/accounts/" + accountId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{accNum}")
    public ResponseEntity<AccountsDto.AccountDetailsResponse> getAccountDetails(
        @PathVariable("accNum") String accountNumber) {
        AccountsDto.AccountDetailsResponse response = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accNum}/balance")
    public ResponseEntity<AccountsDto.AccountBalanceResponse> getAccountBalance(
        @PathParam("accNum") String accountNumber, @RequestParam String param) {
        AccountsDto.AccountBalanceResponse balanceResponse = accountService.getAccountBalance(accountNumber);

        return ResponseEntity.ok(balanceResponse);
    }

}
