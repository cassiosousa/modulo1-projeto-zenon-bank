package br.com.zenon.ingestion.processor;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Customer;
import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionType;
import br.com.zenon.exceptions.TransactionTypeNotFound;

import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionParseLine {
    private final Logger logger = Logger.getLogger(TransactionParseLine.class.getName());
    private final int NO_TOKENS = 0;
    private final CsvTokenizerLine tokenizerLine;

    public TransactionParseLine(CsvTokenizerLine tokenizerLine) {
        this.tokenizerLine = tokenizerLine;
    }

    public Optional<Transaction> parse(String line) {
        StringTokenizer tokenizer = tokenizerLine.tokenizer(line);
        if (tokenizer.countTokens() == NO_TOKENS) {
            return Optional.empty();
        }
        int tokenNumber = 0;
        Map<Integer, Object> mapLine = new HashMap<>();
        while (tokenizer.hasMoreElements()) {
            Object value = tokenizer.nextElement();
            mapLine.put(tokenNumber, value);
            String printLine = tokenNumber + ":" + value;
            //IO.println(printLine);
            tokenNumber += 1;
        }
        try {
            return Optional.of(transform(mapLine));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Transaction transform(Map<Integer, Object> mapLine) throws TransactionTypeNotFound {
        Long step = Long.parseLong(mapLine.get(0).toString());
        TransactionType transactionType = TransactionType.get(mapLine.get(1).toString());
        BigDecimal balance = new BigDecimal(mapLine.get(2).toString());


        return new Transaction(
                step,
                transactionType,
                balance,
                buildCustomerOrigen(mapLine),
                buildCustomerDestination(mapLine),
                getBoolean(mapLine.get(9)),
                getBoolean(mapLine.get(10))
        );
    }

    private Customer buildCustomerOrigen(Map<Integer, Object> mapLine) {
        BigDecimal oldBalance = new BigDecimal(mapLine.get(4).toString());
        BigDecimal newBalance = new BigDecimal(mapLine.get(5).toString());

        return new Customer(
                mapLine.get(3).toString(),
                oldBalance,
                newBalance
        );
    }

    private Customer buildCustomerDestination(Map<Integer, Object> mapLine) {
        BigDecimal oldBalance = new BigDecimal(mapLine.get(7).toString());
        BigDecimal newBalance = new BigDecimal(mapLine.get(8).toString());

        return new Customer(
                mapLine.get(6).toString(),
                oldBalance,
                newBalance
        );
    }

    private boolean getBoolean(Object value) {
        return Integer.parseInt(value.toString()) == 1;
    }
}
