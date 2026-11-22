package br.com.zenon;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Transaction;
import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.ingestion.analizer.*;
import br.com.zenon.ingestion.processor.TransactionLinePipelineValidation;
import br.com.zenon.ingestion.processor.TransactionParseLine;
import br.com.zenon.ingestion.processor.TransactionReader;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        TransactionParseLine transactionParseLine = new TransactionParseLine(new CsvTokenizerLine(), new TransactionLinePipelineValidation());
        TransactionReader transactionReader = new TransactionReader(transactionParseLine);
        TransactionIngestor transactionIngestor = new TransactionIngestor(transactionReader);

        List<Transaction> transactions = transactionIngestor.transactions("PS_20174392719_1491204439457_log.csv", 50_000).get();
        fraudAnalitics(transactions);

    }
    private static  void fraudAnalitics(List<Transaction> transactions){
        new FraudAnalyzerQuantity().analyzer(transactions);
        new FraudAnalyzerBiggest().analyzer(transactions);
        new FraudAnalyzerBiggestFive().analyzer(transactions);
        new FraudAnalyzerLossAmount().analyzer(transactions);
        new FraudAnalyzerByType().analyzer(transactions);
    }
}
