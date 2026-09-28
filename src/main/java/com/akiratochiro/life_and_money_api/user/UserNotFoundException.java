package com.akiratochiro.life_and_money_api.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found, id: " + id);
    }
}
