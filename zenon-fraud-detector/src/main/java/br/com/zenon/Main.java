package br.com.zenon;

import br.com.zenon.domain.Customer;
import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionStep;
import br.com.zenon.domain.TransactionType;

import java.math.BigDecimal;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        List<Transaction> transactions = buildTransactions();

        for (var transaction : transactions) {
            IO.println(transaction);
        }
    }

    private static  List<Transaction> buildTransactions() {
        Transaction transaction1 = new Transaction(
                1,
                TransactionType.PAYMENT,
                new BigDecimal(9839.64),
                new Customer(
                        "C1231006815", new BigDecimal(170136.0), new BigDecimal(160296.36)
                ),
                new Customer(
                        "M1979787155", new BigDecimal(0.0), new BigDecimal(0.0)
                ),
                false,
                false);
        Transaction transaction2 = new Transaction(
                743,
                TransactionType.CASH_OUT,
                new BigDecimal(850002.52),
                new Customer(
                        "C1280323807", new BigDecimal(850002.52), new BigDecimal(0.0)
                ),
                new Customer(
                        "C873221189", new BigDecimal(6510099.11), new BigDecimal(7360101.63)
                ),
                true,
                false);
        return List.of(transaction1, transaction2);
    }

}
