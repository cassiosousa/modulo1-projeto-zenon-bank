package br.com.zenon.domain;

import java.math.BigDecimal;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
}
