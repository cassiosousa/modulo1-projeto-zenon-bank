package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;

import java.util.List;

public class FraudAnalyzerQuantity implements FraudAnalyzer {
    @Override
    public void analyzer(List<Transaction> transactions) {
        long count = transactions.parallelStream().filter(Transaction::isFraud).count();
        IO.println("1. Total de Fraudes:" + count);
    }
}
