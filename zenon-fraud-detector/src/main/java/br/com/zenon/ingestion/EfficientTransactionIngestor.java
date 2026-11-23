package br.com.zenon.ingestion;

import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.processor.ConsumeChunkRead;
import br.com.zenon.ingestion.processor.TransactionsReaderPipeLine;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EfficientTransactionIngestor {
    private Logger logger = Logger.getLogger(EfficientTransactionIngestor.class.getName());
    private final TransactionsReaderPipeLine transactionsReaderPipeLine;
    private static final Semaphore permits = new Semaphore(6, true);

    public EfficientTransactionIngestor(TransactionsReaderPipeLine transactionsReaderPipeLine) {
        this.transactionsReaderPipeLine = transactionsReaderPipeLine;
    }

    public Optional<List<Transaction>> transactions(String file, int chunkSize, ConsumeChunkRead<Transaction> consumeChunkRead) {

        BufferedReader bufferedReader = transactionsReaderPipeLine.readerFile(file);
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor(); bufferedReader) {
            long totalLines = 0;
            //skip first line
            bufferedReader.readLine();
            String line = null;
            List<String> linesProcess = new ArrayList<>(chunkSize);
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                linesProcess.add(line);
                totalLines++;
                if (linesProcess.size() == chunkSize) {
                    sendChunk(chunkSize, consumeChunkRead, totalLines, executor, linesProcess);
                    linesProcess = new ArrayList<>(chunkSize);
                }
            }
            if (!linesProcess.isEmpty()) {
                sendChunk(chunkSize, consumeChunkRead, totalLines, executor, linesProcess);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private void sendChunk(int chunkSize, ConsumeChunkRead<Transaction> consumeChunkRead, long totalLines, ExecutorService executor, List<String> linesProcess) throws InterruptedException {
        IO.println("waiting:" + permits.availablePermits());
        permits.acquire();
        IO.println("executing:" + totalLines);
        executor.submit(() -> executePipeline(new ArrayList<>(linesProcess), chunkSize, consumeChunkRead));
    }

    private void executePipeline(List<String> lines, long chunkSize, ConsumeChunkRead<Transaction> consumeChunkRead) {
        IO.println("Executing chunk" + chunkSize);
        try {
            transactionsReaderPipeLine.readTransactions(lines, chunkSize, consumeChunkRead);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            permits.release();
        }
    }

}
