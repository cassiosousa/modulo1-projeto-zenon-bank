package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransactionsReaderPipeLine {
    BufferedReader readerFile(String file);

    Optional<List<Transaction>> readTransactions(List<String> lines,  long chunkSize, ConsumeChunkRead<Transaction> consumeChunkRead) throws IOException;
}
