package br.com.zenon.persistence.mapping;

import br.com.zenon.domain.Customer;
import br.com.zenon.domain.Transaction;
import br.com.zenon.persistence.entities.CustomerEntity;
import br.com.zenon.persistence.entities.TransactionEntity;

public class MappingTransaction {
    public static TransactionEntity to(Transaction transaction) {
        return new TransactionEntity(
                transaction.id(),
                transaction.step(),
                transaction.type(),
                transaction.amount(),
                new CustomerEntity(
                        transaction.customerOrigin().name(),
                        transaction.customerOrigin().oldBalance(),
                        transaction.customerOrigin().newBalance()
                ),
                new CustomerEntity(
                        transaction.customerDestination().name(),
                        transaction.customerDestination().oldBalance(),
                        transaction.customerDestination().newBalance()
                ),
                transaction.isFraud(),
                transaction.isFlaggedFraud()
        );
    }

    public static Transaction to(TransactionEntity entity) {
        return new Transaction(
                entity.id(),
                entity.step(),
                entity.type(),
                entity.amount(),
                new Customer(
                        entity.customerOrigin().name(),
                        entity.customerOrigin().oldBalance(),
                        entity.customerOrigin().newBalance()
                ),
                new Customer(
                        entity.customerDestination().name(),
                        entity.customerDestination().oldBalance(),
                        entity.customerDestination().newBalance()
                ),
                entity.isFraud(),
                entity.isFlaggedFraud()
        );
    }
}
