package br.com.zenon.persistence;

import br.com.zenon.persistence.entities.TransactionEntity;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
    private List<TransactionEntity> transactions;

    public TransactionListRepository(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<TransactionEntity> findByNameOrigin(String name) {
        return transactions.stream()
                .filter(item -> item.customerOrigin().name().contains(name))
                .findFirst();
    }

    @Override
    public TransactionEntity save(TransactionEntity transaction) {
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public List<TransactionEntity> saveAll(List<TransactionEntity> transaction) {
        return null;
    }
}
