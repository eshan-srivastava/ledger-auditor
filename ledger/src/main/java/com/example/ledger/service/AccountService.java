package com.example.ledger.service;

import org.springframework.stereotype.Service;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.exception.AccountException;
import com.example.ledger.exception.UserException;
import com.example.ledger.mapper.AccountMapper;
import com.example.ledger.model.Account;
import com.example.ledger.model.User;
import com.example.ledger.repository.AccountRepo;
import com.example.ledger.repository.UserRepo;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final UserRepo userRepo;

    public AccountService(AccountRepo repo, AccountMapper accMapper, UserRepo userRepo) {
        this.accountRepo = repo;
        this.userRepo = userRepo;
        this.accountMapper = accMapper;
    };

    public Account createAccount(AccountsDto.CreateAccountRequest request) {
        User user = userRepo.findById(request.userId())
            .orElseThrow(() -> new UserException.NotFound(request.userId()));

        if (accountRepo.existsByAccountNumber(request.accountNumer())) {
            throw new AccountException.AlreadyExists("Account number already exists");
        }

        Account account = new Account();
        account.setAccountNumber(request.accountNumer());
        account.setType(request.accountType());
        account.setUser(user);
        return accountRepo.save(account);
    }

    public AccountsDto.AccountBalanceResponse calculateAccountBalance(String accountNumber) {
        return null;
    }
}
