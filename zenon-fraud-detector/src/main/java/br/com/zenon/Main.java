package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Customer;
import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionStep;
import br.com.zenon.domain.TransactionType;
import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.ingestion.processor.TransactionParseLine;
import br.com.zenon.ingestion.processor.TransactionReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine());
        TransactionReader transactionReader = new TransactionReader(transactionParseLine);
        TransactionIngestor transactionIngestor = new TransactionIngestor(transactionReader);

        List<Transaction> transactions = transactionIngestor.transactions("PS_20174392719_1491204439457_log.csv").get();
        for (int i = 0; i < 10; i++) {
            IO.println(transactions.get(i));
        }

    }
}
