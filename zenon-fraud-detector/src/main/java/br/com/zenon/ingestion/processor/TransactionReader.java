package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.Transaction;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TransactionReader implements TransactionsReaderPipeLine {
    private Logger logger = Logger.getLogger(TransactionReader.class.getName());

    private final TransactionParseLine transactionParseLine;

    public TransactionReader(TransactionParseLine transactionParseLine) {
        this.transactionParseLine = transactionParseLine;
    }

    @Override
    public BufferedReader readerFile(String file) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, Charset.forName("UTF-8"));
        return new BufferedReader(inputStreamReader);
    }

    @Override
    public Optional<List<Transaction>> readTransactions(List<String> lines, long chunkSize, ConsumeChunkRead<Transaction> consumeChunkRead) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            transactionParseLine.parse(line).ifPresent(transactions::add);
        }
        consumeChunkRead.write(transactions);
        return Optional.of(transactions);
    }
}
