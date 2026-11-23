package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.EfficientTransactionIngestor;
import br.com.zenon.ingestion.processor.TransactionLinePipelineValidation;
import br.com.zenon.ingestion.processor.TransactionParseLine;
import br.com.zenon.ingestion.processor.TransactionReader;
import br.com.zenon.persistence.TransactionSqlRepository;
import br.com.zenon.persistence.mapping.MappingTransaction;

import java.util.List;

public class DBEfficientMain {

    private static final TransactionSqlRepository transactionSqlRepository = new TransactionSqlRepository();

    void main() {
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine(), new TransactionLinePipelineValidation());
        TransactionReader transactionReader = new TransactionReader(transactionParseLine);
        EfficientTransactionIngestor transactionIngestor = new EfficientTransactionIngestor(transactionReader);
        var start = System.nanoTime();
        transactionIngestor.transactions("PS_20174392719_1491204439457_log.csv", 10_000,
                DBEfficientMain::saveInDatabase);

        var end = System.nanoTime();
        double segundos = (end - start) / 1_000_000_000.0;
        IO.println(String.format("Tempo de execução total: %.3f sec", segundos));
    }

    private static void saveInDatabase(List<Transaction> transactions) {
        IO.println("Recebidas para salvar no banco: " + transactions.size());
        var start = System.nanoTime();
        transactionSqlRepository.saveAll(transactions.stream().map(MappingTransaction::to).toList());
        var end = System.nanoTime();
        double segundos = (end - start) / 1_000_000_000.0;
        IO.println(String.format("Tempo de execução: %.3f sec", segundos));
    }
}
