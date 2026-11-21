package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.Transaction;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;

public interface TransactionsReaderPipeLine {
    BufferedReader readerFile(String file);
    Optional<List<Transaction>> readTransactions(BufferedReader  bufferedFileRead, int chunck);
}
