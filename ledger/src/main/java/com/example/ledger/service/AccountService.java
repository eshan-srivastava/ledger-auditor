package com.example.ledger.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ledger.dto.AccountsDto;
import com.example.ledger.exception.AccountException;
import com.example.ledger.exception.HttpException;
import com.example.ledger.exception.UserException;
import com.example.ledger.mapper.AccountMapper;
import com.example.ledger.model.Account;
import com.example.ledger.model.User;
import com.example.ledger.repository.AccountRepo;
import com.example.ledger.repository.TransactionRepo;
import com.example.ledger.repository.UserRepo;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;

    public AccountService(AccountRepo repo, AccountMapper accMapper, UserRepo userRepo, TransactionRepo txnRepo) {
        this.accountRepo = repo;
        this.userRepo = userRepo;
        this.accountMapper = accMapper;
        this.transactionRepo = txnRepo;
    };

    public Long createAccount(AccountsDto.CreateAccountRequest request) {
        User user = userRepo.findById(request.userId())
            .orElseThrow(() -> new UserException.NotFound(request.userId()));

        if (accountRepo.existsByAccountNumber(request.accountNumer())) {
            throw new AccountException.AlreadyExists("Account number already exists");
        }

        Account account = accountMapper.toEntity(request, user);
        if (account == null) {
            throw new HttpException.InternalError("invalid account request");
        }

        Account savedAcc = accountRepo.save(account);
        return savedAcc.getId();
    }

    public AccountsDto.AccountDetailsResponse getAccountDetails(String accountNumber) {

        Account account = accountRepo.findByAccountNumber(accountNumber).orElseThrow(
            () -> new AccountException.NotFound(accountNumber));

        BigDecimal balance = transactionRepo.calculateNetBalance(List.of(account.getId()));

        return accountMapper.toDetailsResponse(account, balance);
    }

    public AccountsDto.AccountBalanceResponse getAccountBalance(String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber).orElseThrow(
            () -> new AccountException.NotFound(accountNumber));

        BigDecimal balance = transactionRepo.calculateNetBalance(List.of(account.getId()));

        return accountMapper.toBalanceResponse(account, balance);
    }

    public BigDecimal getBalanceForUserId(Long userId) {
        BigDecimal amount;
        List<Account> accounts = accountRepo.findAllByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            amount = BigDecimal.valueOf(0);
        } else {
            List<Long> ids = accounts.stream()
                .map(Account::getId).toList();
            amount = transactionRepo.calculateNetBalance(ids);
        }
        return amount;
    }

    public Account getAccountByNumber(String accNumber) {
        return accountRepo.findByAccountNumber(accNumber).orElseThrow(() -> new AccountException.NotFound(accNumber));
    }
}
