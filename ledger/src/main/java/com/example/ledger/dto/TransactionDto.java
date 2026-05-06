package com.example.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDto {
    // This class will expose request response for transaction business routes

    public record TransactionsListResponseElem(

    ) {
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
        Integer amount) {
    }
}
