package br.com.zenon.persistence;

import br.com.zenon.persistence.entities.TransactionEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {
    private Map<String, TransactionEntity> transactions;

    public TransactionMapRepository(Map<String, TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<TransactionEntity> findByNameOrigin(String name) {
        return Optional.ofNullable(transactions.get(name));
    }

    @Override
    public TransactionEntity save(TransactionEntity transaction) {
        transactions.put(transaction.customerOrigin().name(), transaction);
        return transaction;
    }

    @Override
    public List<TransactionEntity> saveAll(List<TransactionEntity> transaction) {
        return null;
    }
}
