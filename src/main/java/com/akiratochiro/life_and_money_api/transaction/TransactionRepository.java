package com.akiratochiro.life_and_money_api.transaction;

import com.akiratochiro.life_and_money_api.category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    List<Transaction> findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
            Long userId, LocalDate start, LocalDate end);
    @Query("""
        select new com.akiratochiro.life_and_money_api.transaction.CategoryTotal(c.id, c.name, sum(t.amount))
        from Transaction t
        join Category c on c.id = t.categoryId
        where t.userId = :userId
          and c.type = :type
          and t.transactionDate between :start and :end
        group by c.id, c.name
        order by sum(t.amount) desc
        """)
    List<CategoryTotal> sumByCategory(@Param("userId") Long userId,
                                      @Param("type") CategoryType type,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);
}
