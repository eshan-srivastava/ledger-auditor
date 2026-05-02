package com.example.ledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.TransactionDto;

import jakarta.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * @param
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @GetMapping("/")
    public ResponseEntity<TransactionDto.TransactionsListResponse> getTransactionsListWithFilters(
        @RequestParam String param) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto.TransactionDetailsResponse> getTransactionById(@PathParam("id") String txnID) {

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionDto.CreateRequest reqbody) {
        // TODO: process POST request

        return ResponseEntity.ok(null);
    }

}
