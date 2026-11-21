package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.Transaction;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionReader implements TransactionsReaderPipeLine {
    private Logger logger = Logger.getLogger(TransactionReader.class.getName());

    private final TransactionParseLine transactionParseLine;

    public TransactionReader(TransactionParseLine transactionParseLine) {
        this.transactionParseLine = transactionParseLine;
    }

    @Override
    public BufferedReader readerFile(String file) {
        InputStream resourceAsStream =this.getClass().getClassLoader().getResourceAsStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, Charset.forName("UTF-8"));
        return new BufferedReader(inputStreamReader);
    }

    @Override
    public Optional<List<Transaction>> readTransactions(BufferedReader bufferedFileRead, int chunck) {
        String line = null;

        try (bufferedFileRead;) {
            List<Transaction> transactions = new ArrayList<>();
            int countLines = 0;
            while ((line = bufferedFileRead.readLine()) != null && countLines < chunck) {
                if(countLines == 0 ){
                    countLines++;
                    continue;
                }
                transactionParseLine.parse(line).ifPresent(parsedTransaction -> {
                    transactions.add(parsedTransaction);
                });
                countLines++;
            }
            if (!transactions.isEmpty()) {
                return Optional.of(transactions);
            }
            return Optional.empty();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
