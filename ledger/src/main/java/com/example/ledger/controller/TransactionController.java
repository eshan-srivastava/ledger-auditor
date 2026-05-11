package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.TransactionDto;
import com.example.ledger.service.TransactionService;

import jakarta.websocket.server.PathParam;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Validated
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService txService) {
        this.transactionService = txService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<TransactionDto.TxnListResponseElem>> getTransactionsListWithFilters(
        @RequestParam(required = false) String sourceAccNum,
        @RequestParam(required = false) String destAccNum,
        @RequestParam(required = false) Long originId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size

    ) {
        TransactionDto.TransactionFilter filter = new TransactionDto.TransactionFilter(
            sourceAccNum,
            destAccNum, originId, fromDate, toDate);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionDto.TxnListResponseElem> response = transactionService.getTransactions(filter, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto.TransactionDetailsResponse> getTransactionById(@PathParam("id") Long txnID) {
        TransactionDto.TransactionDetailsResponse response = transactionService.getTransactionDetails(txnID);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionDto.CreateRequest reqbody) {
        Long createdId = transactionService.createTransaction(reqbody);
        URI location = URI.create("/api/transactions/" + createdId);
        return ResponseEntity.created(location).build();
    }

}
