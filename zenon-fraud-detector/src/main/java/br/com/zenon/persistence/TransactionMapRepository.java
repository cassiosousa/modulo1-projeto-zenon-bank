package br.com.zenon.persistence;

import br.com.zenon.domain.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {
    private Map<String, Transaction> transactions;

    public TransactionMapRepository(Map<String, Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByNameOrigin(String name) {
        return Optional.ofNullable(transactions.get(name));
    }
}
