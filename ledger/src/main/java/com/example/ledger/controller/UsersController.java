package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.UserDto;
import com.example.ledger.mapper.UserMapper;
import com.example.ledger.model.User;
import com.example.ledger.service.TransactionService;
import com.example.ledger.service.UserService;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Validated
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final UserMapper userMapper;

    public UsersController(
        UserService userService,
        UserMapper userMapper,
        TransactionService tService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.transactionService = tService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserDto.UserDetailsResponse> getUserDetails(@PathVariable() long userID,
        @RequestParam String param) {
        UserDto.UserDetailsResponse response = userService.getUserDetails(userID);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userID}/balance")
    public ResponseEntity<UserDto.BalanceResponse> getUserBalance(@PathVariable() long userID) {
        User user = userService.getUser(userID);
        BigDecimal balance = transactionService.getBalanceForUser(userID);
        UserDto.BalanceResponse response = userMapper.toBalanceResponse(user, balance);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto.CreateUserResponse> createUser(@Valid @RequestBody UserDto.CreateRequest reqbody) {
        UserDto.CreateUserResponse response = userService.createUser(reqbody); // thows on failure
        URI location = URI.create("/api/users/" + response.userId());
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{userID}")
    public ResponseEntity<Void> updateUser(@PathVariable("userID") long userId,
        @RequestBody UserDto.UpdateRequest reqBody) {
        userService.updateUser(userId, reqBody);
        return ResponseEntity.ok(null);
    }
}