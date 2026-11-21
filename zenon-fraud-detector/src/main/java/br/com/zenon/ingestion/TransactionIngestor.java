package br.com.zenon.ingestion;

import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.processor.TransactionsReaderPipeLine;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TransactionIngestor {
    private Logger logger = Logger.getLogger(TransactionIngestor.class.getName());
    private final TransactionsReaderPipeLine transactionsReaderPipeLine;

    public TransactionIngestor(TransactionsReaderPipeLine transactionsReaderPipeLine) {
        this.transactionsReaderPipeLine = transactionsReaderPipeLine;
    }

    public Optional<List<Transaction>> transactions(String file) {
        BufferedReader bufferedReader = transactionsReaderPipeLine.readerFile(file);
        return transactionsReaderPipeLine.readTransactions(bufferedReader, 1000);

    }

}
