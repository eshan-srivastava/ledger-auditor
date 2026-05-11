package com.example.ledger.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.ledger.dto.TransactionDto;
import com.example.ledger.model.Account;
import com.example.ledger.model.Transaction;

@Component
public class TransactionMapper {

    public Transaction toEntity(
        Integer amt,
        Account sourceAccount,
        Account destAccount,
        Long originId) {

        BigDecimal bdamount = BigDecimal.valueOf(amt);
        Transaction txn = new Transaction(
            bdamount,
            sourceAccount,
            destAccount,
            originId);

        return txn;
    }

    public TransactionDto.TransactionDetailsResponse toDetailsResponse(Transaction txn) {
        if (txn == null) {
            return null;
        }

        return new TransactionDto.TransactionDetailsResponse(
            txn.getId(),
            txn.getAmount(),
            txn.getCreatedAt(),
            txn.getOriginId(),
            txn.getSourceAccount().getAccountNumber(),
            txn.getDestinationAccount().getAccountNumber());
    }

    public TransactionDto.TxnListResponseElem toListResponseElem(Transaction txn) {
        if (txn == null) {
            return null;
        }

        return new TransactionDto.TxnListResponseElem(
            txn.getId(),
            txn.getAmount(),
            txn.getCreatedAt(),
            txn.getOriginId(),
            txn.getSourceAccount().getAccountNumber(),
            txn.getDestinationAccount().getAccountNumber());
    }
}
