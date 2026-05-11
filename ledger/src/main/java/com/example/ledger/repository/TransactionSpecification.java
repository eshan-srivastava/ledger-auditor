package com.example.ledger.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.ledger.dto.TransactionDto;
import com.example.ledger.model.Account;
import com.example.ledger.model.Transaction;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(TransactionDto.TransactionFilter filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // the pattern is that if filter has the value we add it to predicates using the
            // add syntax
            if (filter.sourceAccNumber() != null && !filter.sourceAccNumber().isBlank()) {
                Join<Transaction, Account> accountJoin = root.join("source_account_id");
                predicates.add(cb.equal(accountJoin.get("accountNumber"), filter.sourceAccNumber()));
            }

            if (filter.destAccNumber() != null && !filter.destAccNumber().isBlank()) {
                Join<Transaction, Account> accountJoin = root.join("destination_account_id");
                predicates.add(cb.equal(accountJoin.get("destAccNum"), filter.sourceAccNumber()));
            }

            if (filter.originId() != null) {
                predicates.add(cb.equal(root.get("originId"), filter.originId()));
            }

            if (filter.fromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    root.get("transactionDate"),
                    filter.fromDate().atStartOfDay()));
            }

            if (filter.fromDate() != null) {
                predicates.add(cb.lessThan(
                    root.get("transactionDate"),
                    filter.toDate().plusDays(1).atStartOfDay()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
