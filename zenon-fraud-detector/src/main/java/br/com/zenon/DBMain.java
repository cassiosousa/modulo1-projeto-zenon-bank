package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.ingestion.processor.TransactionLinePipelineValidation;
import br.com.zenon.ingestion.processor.TransactionParseLine;
import br.com.zenon.ingestion.processor.TransactionReader;
import br.com.zenon.persistence.TransactionSqlRepository;
import br.com.zenon.persistence.mapping.MappingTransaction;

import java.util.List;

public class DBMain {
    void main() {
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine(), new TransactionLinePipelineValidation());
        TransactionReader transactionReader = new TransactionReader(transactionParseLine);
        TransactionIngestor transactionIngestor = new TransactionIngestor(transactionReader);

        List<Transaction> transactions = transactionIngestor
                .transactions("PS_20174392719_1491204439457_log.csv", 10_000).get();

        saveInDatabase(transactions);
    }

    private static void saveInDatabase(List<Transaction> transactions) {
        TransactionSqlRepository transactionSqlRepository = new TransactionSqlRepository();
        var start = System.nanoTime();
        transactions.forEach(transaction -> {
            transactionSqlRepository.save(MappingTransaction.to(transaction));
        });

        var end = System.nanoTime();
        double segundos = (end - start) / 1_000_000_000.0;
        IO.println(String.format("Tempo de execução: %.3f sec", segundos));
    }
}
