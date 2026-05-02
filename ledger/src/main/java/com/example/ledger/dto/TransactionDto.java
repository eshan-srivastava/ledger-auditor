package com.example.ledger.dto;

public class TransactionDto {
    // This class will expose request response for transaction business routes

    public record TransactionsListResponse() {
    }

    public record TransactionDetailsResponse(

    ) {
    }

    public record CreateRequest(
        String sourceAccountNum,
        String destinationAccountNum,
        Long originId,
        Integer amount) {
    }
}
