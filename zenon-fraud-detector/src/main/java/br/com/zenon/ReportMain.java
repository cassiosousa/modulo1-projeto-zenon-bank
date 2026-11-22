package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.processor.TransactionLinePipelineValidation;
import br.com.zenon.ingestion.processor.TransactionParseLine;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class ReportMain {

    void main() {
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine(), new TransactionLinePipelineValidation());
        URL resourceAsStream = this.getClass().getClassLoader().getResource("PS_20174392719_1491204439457_log.csv");
        try (Stream<String> lines = java.nio.file.Files.lines(Path.of(resourceAsStream.toURI()));) {
            var start = System.nanoTime();
            lines.skip(1)
                    .map(transactionParseLine::parse)
                    .map(Optional::get)
                    .gather(transactionsSummarize())
                    .findFirst()
                    .ifPresent(IO::println);
            var end = System.nanoTime();
            double segundos = (end - start) / 1_000_000_000.0;
            IO.println(String.format("Tempo de execução: %.3f sec", segundos));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static Gatherer<Transaction, TransactionCalculate, TransactionCalculate> transactionsSummarize() {
        Gatherer<Transaction, TransactionCalculate, TransactionCalculate> gatherer = Gatherer.of(
                TransactionCalculate::new, // estado inicial
                Gatherer.Integrator.ofGreedy((state, transaction, _) -> {
                            state.addTotalBalance(transaction);
                            state.addTotalFraud(transaction);
                            state.addTotalLines();
                            return true;
                        }
                ),
                TransactionCalculate::combine,
                (state, downstream) -> downstream.push(state)
        );
        return gatherer;
    }
}

final class TransactionCalculate {
    private long totalLines;
    private long totalFraud;
    private BigDecimal totalAmount;

    TransactionCalculate() {
        this.totalLines = 0;
        this.totalFraud = 0;
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addTotalLines() {
        totalLines++;
    }

    public void addTotalFraud(Transaction transaction) {
        if (transaction.isFraud()) {
            totalFraud++;
        }
    }

    public void addTotalBalance(Transaction transaction) {
        totalAmount = totalAmount.add(transaction.amount());
    }

    public TransactionCalculate combine(TransactionCalculate other) {
        this.totalLines += other.totalLines;
        this.totalFraud += other.totalFraud;
        this.totalAmount = this.totalAmount.add(other.totalAmount);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TransactionCalculate) obj;
        return this.totalLines == that.totalLines &&
                this.totalFraud == that.totalFraud &&
                Objects.equals(this.totalAmount, that.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalLines, totalFraud, totalAmount);
    }

    @Override
    public String toString() {
        return "TransactionCalculate[\n" +
                "Total de linhas:" + totalLines + ", \n" +
                "Total de fraudes:" + totalFraud + ", \n" +
                "Valor total transacionado:" + totalAmount + "\n]";
    }
}