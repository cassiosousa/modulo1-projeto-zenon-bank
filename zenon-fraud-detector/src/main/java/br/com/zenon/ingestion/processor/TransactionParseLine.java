package br.com.zenon.ingestion.processor;

import br.com.zenon.csv.CsvTokenizerLine;
import br.com.zenon.domain.Customer;
import br.com.zenon.domain.Transaction;
import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.domain.TransactionType;
import br.com.zenon.exceptions.TransactionTypeException;
import br.com.zenon.ingestion.processor.validations.PipelineValidation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.zenon.domain.TransactionFieldKey.*;

public class TransactionParseLine {
    private final Logger logger = Logger.getLogger(TransactionParseLine.class.getName());
    private final CsvTokenizerLine tokenizerLine;
    private final PipelineValidation pipelineValidation;

    public TransactionParseLine(CsvTokenizerLine tokenizerLine, PipelineValidation pipelineValidation) {
        this.tokenizerLine = tokenizerLine;
        this.pipelineValidation = pipelineValidation;
    }

    public Optional<Transaction> parse(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }
        StringTokenizer tokenizer = tokenizerLine.tokenizer(line);
        int NO_TOKENS = 0;
        if (tokenizer.countTokens() == NO_TOKENS) {
            return Optional.empty();
        }
        int tokenNumber = 0;
        Map<TransactionFieldKey, Object> mapLine = new HashMap<>(11);
        try {
            while (tokenizer.hasMoreElements()) {
                Object value = tokenizer.nextElement();
                mapLine.put(get(tokenNumber), value);
                tokenNumber += 1;
            }
            pipelineValidation.execute(mapLine);
            return Optional.of(transform(mapLine));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Transaction transform(Map<TransactionFieldKey, Object> mapLine) throws TransactionTypeException {
        Long step = Long.parseLong(mapLine.get(STEP).toString());
        TransactionType transactionType = TransactionType.get(mapLine.get(TYPE).toString());
        BigDecimal balance = new BigDecimal(mapLine.get(AMOUNT).toString());

        return new Transaction(null,
                step,
                transactionType,
                balance,
                buildCustomerOrigin(mapLine),
                buildCustomerDestination(mapLine),
                getBoolean(mapLine.get(IS_FRAUD)),
                getBoolean(mapLine.get(IS_FLAGGED_FRAUD))
        );
    }

    private Customer buildCustomerOrigin(Map<TransactionFieldKey, Object> mapLine) {
        BigDecimal oldBalance = new BigDecimal(mapLine.get(CUSTOMER_ORIGIN_OLD_BALANCE).toString());
        BigDecimal newBalance = new BigDecimal(mapLine.get(CUSTOMER_ORIGIN_NEW_BALANCE).toString());

        return new Customer(
                mapLine.get(CUSTOMER_ORIGIN_NAME).toString(),
                oldBalance,
                newBalance
        );
    }

    private Customer buildCustomerDestination(Map<TransactionFieldKey, Object> mapLine) {
        BigDecimal oldBalance = new BigDecimal(mapLine.get(CUSTOMER_DESTINATION_OLD_BALANCE).toString());
        BigDecimal newBalance = new BigDecimal(mapLine.get(CUSTOMER_DESTINATION_NEW_BALANCE).toString());

        return new Customer(
                mapLine.get(CUSTOMER_DESTINATION_NAME).toString(),
                oldBalance,
                newBalance
        );
    }

    private boolean getBoolean(Object value) {
        return Integer.parseInt(value.toString()) == 1;
    }
}
