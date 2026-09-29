package com.akiratochiro.life_and_money_api.transaction;

import java.math.BigDecimal;

public record CategoryTotal(Long categoryId, String categoryName, BigDecimal total) {
}
