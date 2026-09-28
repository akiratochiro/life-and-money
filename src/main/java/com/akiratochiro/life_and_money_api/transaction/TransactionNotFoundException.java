package com.akiratochiro.life_and_money_api.transaction;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction not found, id: " + id);
    }
}
