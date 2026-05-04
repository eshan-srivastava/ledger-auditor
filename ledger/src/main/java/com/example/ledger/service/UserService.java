package com.example.ledger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ledger.dto.UserDto;
import com.example.ledger.exception.HttpException;
import com.example.ledger.exception.UserException;
import com.example.ledger.mapper.UserMapper;
import com.example.ledger.model.Account;
import com.example.ledger.model.User;
import com.example.ledger.repository.AccountRepo;
import com.example.ledger.repository.TransactionRepo;
import com.example.ledger.repository.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepository;
    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Spring injects the dependencies via constructor
    public UserService(
        UserRepo userRepository,
        TransactionRepo txnRepository,
        AccountRepo accRepository,
        UserMapper userMapper,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepo = txnRepository;
        this.accountRepo = accRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto.BalanceResponse getUserBalance(@NonNull Long userId) {
        // 1. Database logic goes here: fetch the user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException.NotFound(userId));

        BigDecimal amount;
        // 2. Business logic would go here (e.g., calculating pending transactions)
        List<Account> accounts = accountRepo.findAllByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            amount = BigDecimal.valueOf(0);
        } else {
            List<Long> ids = accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());
            amount = transactionRepo.calculateNetBalance(ids);
        }

        // 3. Map the entity to the response DTO and return
        return userMapper.toBalanceResponse(user, amount);
    }

    public UserDto.UserDetailsResponse getUserDetails(@NonNull Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException.NotFound(userId));

        return userMapper.toDetailsResponse(user);
    }

    public Long createUser(UserDto.CreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException.AlreadyExists(request.email());
        }
        User user = userMapper.toEntity(request);
        if (user == null) {
            // handle null somehow because .save method is complaining
            throw new HttpException.InternalError("invalid user details");
        }

        // hash password and change user object's pswd
        user.setHashedPswd(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);
        return user.getId();
    }

    public Void updateUser(@NonNull Long userId, UserDto.UpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException.NotFound(userId));

        user.setName(request.name());
        user = userRepository.save(user);
        return null;
    }
}
