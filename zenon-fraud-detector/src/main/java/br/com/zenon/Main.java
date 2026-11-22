package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.ingestion.analizer.*;
import br.com.zenon.ingestion.processor.TransactionLinePipelineValidation;
import br.com.zenon.ingestion.processor.TransactionParseLine;
import br.com.zenon.ingestion.processor.TransactionReader;
import br.com.zenon.persistence.TransactionListRepository;
import br.com.zenon.persistence.TransactionMapRepository;
import br.com.zenon.persistence.TransactionRepository;
import br.com.zenon.persistence.entities.TransactionEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine(), new TransactionLinePipelineValidation());
        TransactionReader transactionReader = new TransactionReader(transactionParseLine);
        TransactionIngestor transactionIngestor = new TransactionIngestor(transactionReader);

        List<Transaction> transactions = transactionIngestor
                .transactions("PS_20174392719_1491204439457_log.csv", 10_000).get();
        /*Map<String, Transaction> transactionMap = transactions.stream()
                .collect(Collectors.toMap(t -> t.customerOrigin().name(), Function.identity()));
        // fraudAnalitics(transactions);
        filterTransaction(transactions, "C1231006815");
        filterTransaction(transactions, "C1868032458");

        filterTransaction(transactionMap, "C1868032458");*/

    }

    private static void fraudAnalitics(List<Transaction> transactions) {
        new FraudAnalyzerQuantity().analyzer(transactions);
        new FraudAnalyzerBiggest().analyzer(transactions);
        new FraudAnalyzerBiggestFive().analyzer(transactions);
        new FraudAnalyzerLossAmount().analyzer(transactions);
        new FraudAnalyzerByType().analyzer(transactions);
    }

    private static void filterTransaction(List<TransactionEntity> transactions, String value) {
        TransactionRepository transactionRepository = new TransactionListRepository(transactions);
        var start = System.nanoTime();
        Optional<TransactionEntity> result = transactionRepository.findByNameOrigin(value);
        IO.println(result);
        var end = System.nanoTime();
        double segundos = (end - start) / 1_000_000_000.0;
        IO.println(String.format("Tempo de execução: %.3f sec", segundos));
    }

    private static void filterTransaction(Map<String, TransactionEntity> transactions, String value) {
        TransactionRepository transactionRepository = new TransactionMapRepository(transactions);
        var start = System.nanoTime();
        Optional<TransactionEntity> result = transactionRepository.findByNameOrigin(value);
        IO.println(result);
        var end = System.nanoTime();
        double segundos = (end - start) / 1_000_000_000.0;
        IO.println(String.format("Tempo de execução: %.3f sec", segundos));
    }
}
