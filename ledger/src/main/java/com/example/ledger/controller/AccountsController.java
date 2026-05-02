package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.mapper.AccountMapper;
import com.example.ledger.model.Account;
import com.example.ledger.service.AccountService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(AccountService accountSvc) {
        this.accountService = accountSvc;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createAccount(@RequestBody AccountsDto.CreateAccountRequest entity) {
        Account a = accountService.createAccount(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{accNum}/balance")

    public ResponseEntity<AccountsDto.AccountBalanceResponse> getAccountBalance(
        @PathParam("accNum") String accountNumber, @RequestParam String param) {
        AccountsDto.AccountBalanceResponse balanceResponse = accountService.calculateAccountBalance(accountNumber);

        return ResponseEntity.ok(balanceResponse);
    }

}
