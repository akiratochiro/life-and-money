package com.akiratochiro.life_and_money_api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
                           @NotBlank @Email @Size(max = 255) String email,
                           @NotBlank String password) {

}
