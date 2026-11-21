package br.com.zenon.domain;

import java.math.BigDecimal;

public record Transaction(
        Long step,
        TransactionType type,
        BigDecimal amount,
        Customer customerOrigen,
        Customer customerDestination,
        boolean isFraud,
        boolean isFlaggedFraud
) {
}
