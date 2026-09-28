package com.akiratochiro.life_and_money_api.auth;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}