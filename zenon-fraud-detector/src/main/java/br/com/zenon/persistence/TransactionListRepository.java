package br.com.zenon.persistence;

import br.com.zenon.domain.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
    private List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByNameOrigin(String name) {
        return transactions.stream()
                .filter(item -> item.customerOrigin().name().contains(name))
                .findFirst();
    }
}
