package br.com.zenon.persistence;

import br.com.zenon.persistence.entities.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<TransactionEntity> findByNameOrigin(String name);

    TransactionEntity save(TransactionEntity transaction);

    List<TransactionEntity> saveAll(List<TransactionEntity> transaction);
}
