package com.akiratochiro.life_and_money_api.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {

    Optional<BudgetItem> findByUserIdAndCategoryIdAndValidToIsNull(Long userId, Long categoryId);

    @Query("""
            select b from BudgetItem b
            where b.userId = :userId
              and b.validFrom <= :monthStart
              and (b.validTo is null or b.validTo >= :monthStart)
            """)
    List<BudgetItem> findAllValidInMonth(@Param("userId") Long userId,
                                         @Param("monthStart") LocalDate monthStart);
}
