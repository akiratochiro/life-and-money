package com.akiratochiro.life_and_money_api.goal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    Optional<SavingsGoal> findByIdAndUserId(Long id, Long userId);

    List<SavingsGoal> findAllByUserIdOrderByDeadlineAsc(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}