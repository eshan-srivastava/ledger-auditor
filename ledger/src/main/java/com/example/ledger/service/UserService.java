package com.example.ledger.service;

import org.springframework.stereotype.Service;

import com.example.ledger.dto.UserDto;
import com.example.ledger.exception.UserException;
import com.example.ledger.mapper.UserMapper;
import com.example.ledger.model.User;
import com.example.ledger.repository.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepository;
    private final UserMapper userMapper;

    // Spring injects the dependencies via constructor
    public UserService(UserRepo userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto.BalanceResponse getUserBalance(Long userId) {
        // 1. Database logic goes here: fetch the user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException.NotFound(userId));

        // 2. Business logic would go here (e.g., calculating pending transactions)

        // 3. Map the entity to the response DTO and return
        return userMapper.toBalanceResponse(user);
    }

    public UserDto.UserDetailsResponse getUserDetails(Long userId) {
        return null;
    }

    public Long createUser(UserDto.CreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException.AlreadyExists(request.email());
        }

        try {

        }
        return null;
    }

    public boolean updateUser(Long userId, UserDto.UpdateRequest request) {
        return true;
    }
}
