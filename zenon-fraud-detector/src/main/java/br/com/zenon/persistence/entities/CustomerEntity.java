package br.com.zenon.persistence.entities;

import java.math.BigDecimal;

public record CustomerEntity(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
}
