package com.example.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

        public record CreateRequest(
                @NotBlank(message = "Name is required") @Size(min = 2, max = 64) String name,

                @NotBlank(message = "Email is required") @Email(message = "Email format is invalid") String email,

                @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password) {
        }

        public record UpdateRequest(
                @NotBlank(message = "Name is required") @Size(min = 2, max = 64) String name) {
        }

        public record BalanceResponse(
                Long userId,
                String userName,
                BigDecimal balance) {
        }

        public record UserDetailsResponse(
                Long id,
                String name,
                String email,
                Instant createdAt) {

        }

        public record CreateUserResponse(
                Long userId,
                Instant createdAt) {
        }
}
