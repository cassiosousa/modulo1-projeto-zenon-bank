package br.com.zenon.persistence.entities;

import br.com.zenon.domain.TransactionType;

import java.math.BigDecimal;

public record TransactionEntity(
        Long id,
        Long step,
        TransactionType type,
        BigDecimal amount,
        CustomerEntity customerOrigin,
        CustomerEntity customerDestination,
        boolean isFraud,
        boolean isFlaggedFraud
) {
}
