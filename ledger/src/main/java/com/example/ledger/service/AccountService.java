package com.example.ledger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.exception.AccountException;
import com.example.ledger.exception.HttpException;
import com.example.ledger.mapper.AccountMapper;
import com.example.ledger.model.Account;
import com.example.ledger.model.User;
import com.example.ledger.repository.AccountRepo;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final UserService userService;

    public AccountService(AccountRepo repo, AccountMapper accMapper, UserService uService) {
        this.accountRepo = repo;
        this.userService = uService;
        this.accountMapper = accMapper;
    };

    public AccountsDto.CreateAccountResponse createAccount(AccountsDto.CreateAccountRequest request) {
        User user = userService.getUser(request.userId());

        if (accountRepo.existsByAccountNumber(request.accountNumber())) {
            throw new AccountException.AlreadyExists("Account number already exists");
        }

        Account account = accountMapper.toEntity(request, user);
        if (account == null) {
            throw new HttpException.InternalError("invalid account request");
        }

        Account savedAcc = accountRepo.save(account);
        return accountMapper.toCreateAccountResponse(savedAcc);
    }

    public Account getAccountByNumber(String accNumber) {
        return accountRepo.findByAccountNumber(accNumber).orElseThrow(() -> new AccountException.NotFound(accNumber));
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepo.findAllByUserId(userId);
    }
}
