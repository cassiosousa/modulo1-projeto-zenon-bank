package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.Transaction;

import java.util.List;

@FunctionalInterface
public interface ConsumeChunkRead<R> {
    public void write(List<Transaction> transactions);
}
