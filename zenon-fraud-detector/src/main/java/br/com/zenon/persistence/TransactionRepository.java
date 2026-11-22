package br.com.zenon.persistence;

import br.com.zenon.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByNameOrigin(String name);
}
