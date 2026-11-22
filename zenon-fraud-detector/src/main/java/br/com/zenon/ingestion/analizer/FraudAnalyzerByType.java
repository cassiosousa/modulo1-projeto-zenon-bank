package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

public class FraudAnalyzerByType implements FraudAnalyzer {


    @Override
    public void analyzer(List<Transaction> transactions) {
        IO.println("5. Fraudes por Tipo:");
        transactions.parallelStream()
                .filter(FraudAnalyzerByType::fraudByType)
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        Collectors.counting()
                ))
                .entrySet()
                .forEach(entry->{
                    IO.println("- " + entry.getKey() + ": "+entry.getValue());
                });


    }

    private static boolean fraudByType(Transaction item) {
        return item.isFraud() && (item.type().equals(TransactionType.CASH_OUT) || item.type().equals(TransactionType.TRANSFER));
    }
}
