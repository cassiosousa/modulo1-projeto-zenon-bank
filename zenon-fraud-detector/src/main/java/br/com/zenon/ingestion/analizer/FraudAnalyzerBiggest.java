package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;

import java.util.Comparator;
import java.util.List;

public class FraudAnalyzerBiggest implements FraudAnalyzer {
    @Override
    public void analyzer(List<Transaction> transactions) {
        IO.println("2. Top 3 Fraudes de Maior Valor:");
        transactions.parallelStream().filter(Transaction::isFraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList()
                .forEach(item-> IO.println(item.amount().toPlainString()));
    }
}
