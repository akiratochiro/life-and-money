package com.akiratochiro.life_and_money_api.category;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndUserId(Long id, Long userId);

    List<Category> findAllByUserIdOrderByNameAsc(Long userId);

    List<Category> findAllByUserIdAndArchivedFalseOrderByNameAsc(Long userId);

    boolean existsByUserIdAndTypeAndNameIgnoreCaseAndArchivedFalse(Long userId, CategoryType type, String name);
}
