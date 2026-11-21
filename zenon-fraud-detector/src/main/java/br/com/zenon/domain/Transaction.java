package br.com.zenon.domain;

import java.math.BigDecimal;

public record Transaction(
        int step,
        TransactionType type,
        BigDecimal amount,
        Customer customerOrigen,
        Customer customerDestination,
        boolean isFraud,
        boolean isFlaggedFraud
) {
}
