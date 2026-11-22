package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzerBiggestFive implements FraudAnalyzer {
    @Override
    public void analyzer(List<Transaction> transactions) {
        IO.println("3. Clientes Suspeitos:");
        transactions.stream().filter(Transaction::isFraud)
                .collect(Collectors.groupingBy(
                        t -> t.customerOrigin().name(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5)
                .forEach(IO::println);

    }
}
