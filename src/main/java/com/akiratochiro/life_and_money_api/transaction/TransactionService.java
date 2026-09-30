package com.akiratochiro.life_and_money_api.transaction;

import com.akiratochiro.life_and_money_api.category.CategoryService;
import com.akiratochiro.life_and_money_api.category.CategoryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Transaction create(Long userId, Long categoryId, BigDecimal amount,
                              LocalDate transactionDate, String description) {
        categoryService.getActiveOwned(userId, categoryId);
        return transactionRepository.save(
                new Transaction(userId, categoryId, amount, transactionDate, description));
    }

    @Transactional
    public Transaction update(Long userId, Long transactionId, Long categoryId, BigDecimal amount,
                              LocalDate transactionDate, String description) {
        Transaction transaction = findOwned(userId, transactionId);

        if (!transaction.getCategoryId().equals(categoryId)) {
            categoryService.getActiveOwned(userId, categoryId);
        }

        transaction.update(categoryId, amount, transactionDate, description);
        return transaction;
    }

    private Transaction findOwned(Long userId, Long transactionId) {
        return transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }


    @Transactional(readOnly = true)
    public List<Transaction> list(Long userId, YearMonth month){
        return transactionRepository.findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(userId, month.atDay(1), month.atEndOfMonth());
    }

    @Transactional
    public void delete(Long userId, Long transactionId){
        Transaction transaction = findOwned(userId, transactionId);
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public List<CategoryTotal> sumByCategory(Long userId, CategoryType type, YearMonth month) {
        return transactionRepository.sumByCategory(userId, type, month.atDay(1), month.atEndOfMonth());
    }

    @Transactional(readOnly = true)
    public List<CategoryTotal> sumExpensesByCategory(Long userId, YearMonth month) {
        return sumByCategory(userId, CategoryType.EXPENSE, month);
    }

    @Transactional(readOnly = true)
    public List<MonthlyTypeTotal> sumByMonthAndType(Long userId, YearMonth from, YearMonth to) {
        return transactionRepository.sumByMonthAndType(userId, from.atDay(1), to.atEndOfMonth());
    }


}
