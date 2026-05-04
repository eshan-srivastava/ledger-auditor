package com.example.ledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.model.Account;
import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {

    List<Account> findAllByUserId(Long userId);

    boolean existsByAccountNumber(String accountNumber);
}
