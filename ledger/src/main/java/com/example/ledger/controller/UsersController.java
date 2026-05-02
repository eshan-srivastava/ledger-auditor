package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.UserDto;
import com.example.ledger.service.UserService;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserDto.UserDetailsResponse> getUserDetails(@PathVariable("userID") Long userID,
        @RequestParam String param) {
        UserDto.UserDetailsResponse response = userService.getUserDetails(userID);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userID}/balance")
    public ResponseEntity<UserDto.BalanceResponse> getUserBalance(@PathVariable("userID") Long userID) {
        UserDto.BalanceResponse response = userService.getUserBalance(userID);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createUser(@RequestBody UserDto.CreateRequest reqbody) {
        Long id = userService.createUser(reqbody); // thows on failure
        // common pattern of returning URI of created resource
        URI location = URI.create("/users/" + id);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{userID}")
    public ResponseEntity<Void> updateUser(@PathVariable("userID") Long userId,
        @RequestBody UserDto.UpdateRequest reqBody) {
        var updated = userService.updateUser(userId, reqBody);
        return ResponseEntity.ok(null);
    }
}