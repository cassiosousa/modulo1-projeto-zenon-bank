package br.com.zenon.domain;

import br.com.zenon.exceptions.TransactionTypeNotFound;

import java.util.Objects;

public enum TransactionType {
    PAYMENT, TRANSFER, CASH_OUT, CASH_IN, DEBIT;

    public static TransactionType get(String value) throws TransactionTypeNotFound {
        Objects.requireNonNull(value);
        try {
            return TransactionType.valueOf(value);
        } catch (Throwable e) {
            throw new TransactionTypeNotFound("Transaction Type:" + value + " not found.");
        }
    }
}
