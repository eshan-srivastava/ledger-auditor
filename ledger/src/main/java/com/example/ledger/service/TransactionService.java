package com.example.ledger.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.ledger.dto.TransactionDto;
import com.example.ledger.exception.HttpException;
import com.example.ledger.exception.TransactionException;
import com.example.ledger.mapper.TransactionMapper;
import com.example.ledger.model.Account;
import com.example.ledger.model.Transaction;
import com.example.ledger.repository.TransactionRepo;
import com.example.ledger.repository.TransactionSpecification;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;

    public TransactionService(
        TransactionRepo txnRepo,
        TransactionMapper txnMapper,
        AccountService accService) {
        this.transactionRepo = txnRepo;
        this.transactionMapper = txnMapper;
        this.accountService = accService;
    }

    public Page<TransactionDto.TxnListResponseElem> getTransactions(
        TransactionDto.TransactionFilter filter,
        Pageable pageable) {
        if (pageable == null) {
            throw new HttpException.InternalError("pagination failed");
        }
        return transactionRepo.findAll(TransactionSpecification.withFilters(filter), pageable)
            .map(transactionMapper::toListResponseElem);
    }

    public TransactionDto.TransactionDetailsResponse getTransactionDetails(@NonNull Long id) {
        Transaction transaction = transactionRepo.findById(id).orElseThrow(() -> new TransactionException.NotFound(id));
        return transactionMapper.toDetailsResponse(transaction);
    }

    public TransactionDto.CreateTransactionResponse createTransaction(TransactionDto.CreateRequest request) {
        // first get both accounts and verify if they exist
        Account sourceAccount = accountService.getAccountByNumber(request.sourceAccountNum());
        Account destinationAccount = accountService.getAccountByNumber(request.destinationAccountNum());

        Transaction tx = transactionMapper.toEntity(request.amount(), sourceAccount, destinationAccount,
            request.originId(), request.note());

        if (tx == null) {
            // handle null somehow because .save method is complaining
            throw new HttpException.InternalError("error creating transaction details");
        }
        Transaction savedTx = transactionRepo.save(tx);
        return transactionMapper.toCreateTransactionResponse(savedTx);
    }

    public BigDecimal getBalanceForUser(Long userId) {
        BigDecimal amount;
        List<Long> ids = accountService.getUserAccounts(userId).stream().map(Account::getId).toList();
        if (ids == null || ids.isEmpty()) {
            return BigDecimal.ZERO;
        }
        amount = transactionRepo.calculateNetBalance(ids);
        return amount;
    }

    public BigDecimal getBalanceForAccount(Long accountId) {
        return transactionRepo.calculateNetBalance(List.of(accountId));
    }
}
