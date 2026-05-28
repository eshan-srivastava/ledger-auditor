package com.example.ledger.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ledger.model.Account;
import com.example.ledger.model.Transaction;

@Repository
public interface TransactionRepo extends
  JpaRepository<Transaction, Long>,
  JpaSpecificationExecutor<Transaction> {

  List<Transaction> findBySourceAccountIn(List<Account> accounts);

  List<Transaction> findByDestinationAccountIn(List<Account> accounts);

  List<Transaction> findByOriginId(Long originId);

  @Query("""
       SELECT t FROM Transaction t
       WHERE t.sourceAccount IN :accounts
       OR t.destinationAccount IN :accounts
    """)
  List<Transaction> findAllByAccounts(@Param("accounts") List<Account> accounts);

  @Query("""
    SELECT
            COALESCE(SUM(CASE WHEN t.destinationAccount.id IN :accountIds THEN t.amount ELSE 0 END), 0)
          - COALESCE(SUM(CASE WHEN t.sourceAccount.id IN :accountIds THEN t.amount ELSE 0 END), 0)
        FROM Transaction t
        WHERE t.sourceAccount.id IN :accountIds
           OR t.destinationAccount.id IN :accountIds
            """)
  BigDecimal calculateNetBalance(@Param("accountIds") List<Long> accountIds);

}
