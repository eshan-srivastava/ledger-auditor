package com.example.ledger.mapper;

import com.example.ledger.model.User;
import com.example.ledger.dto.UserDto;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Converts incoming DTO to Entity for saving to the database
    public User toEntity(UserDto.CreateRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User(
            request.name(),
            request.email());
        user.setCreatedAt(Instant.now());
        return user;
    }

    public UserDto.UserDetailsResponse toDetailsResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto.UserDetailsResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt());
    }

    public UserDto.BalanceResponse toBalanceResponse(User user, BigDecimal balance) {
        if (user == null)
            return null;

        return new UserDto.BalanceResponse(
            user.getId(),
            user.getName(),
            new BigDecimal(10));
    }
}
