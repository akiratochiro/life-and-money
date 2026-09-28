package com.akiratochiro.life_and_money_api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    List<Transaction> findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
            Long userId, LocalDate start, LocalDate end);
}
