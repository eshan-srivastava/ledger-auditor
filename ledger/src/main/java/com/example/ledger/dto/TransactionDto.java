package com.example.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class TransactionDto {
    // This class will expose request response for transaction business routes

    public record TransactionsListResponse(
        List<TxnListResponseElem> transactions,
        int total) {
    }

    public record TxnListResponseElem(
        Long id,
        BigDecimal amount,
        Instant timestamp,
        Long originId,
        String sourceAccountNum,
        String destinationAccountNum) {
    }

    public record TransactionDetailsResponse(
        Long id,
        BigDecimal amount,
        Instant timestamp,
        Long originId,
        String sourceAccountNum,
        String destinationAccountNum) {
    }

    public record CreateRequest(
        String sourceAccountNum,
        String destinationAccountNum,
        Long originId,
        Integer amount,
        String note) {
    }

    public record CreateTransactionResponse(
        Long id,
        Instant createdAt) {
    }

    public record TransactionFilter(
        String sourceAccNumber,
        String destAccNumber,
        Long originId,
        LocalDate fromDate,
        LocalDate toDate) {
    }
}
