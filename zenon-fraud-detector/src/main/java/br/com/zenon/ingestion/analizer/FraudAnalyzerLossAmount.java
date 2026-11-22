package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class FraudAnalyzerLossAmount implements FraudAnalyzer {
    @Override
    public void analyzer(List<Transaction> transactions) {
        BigDecimal total = transactions.parallelStream().filter(Transaction::isFraud)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        IO.println("4. Prejuízo Total:" + total);
    }
}
