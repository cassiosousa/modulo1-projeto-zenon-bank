package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.AmountException;
import br.com.zenon.exceptions.IndexException;
import br.com.zenon.exceptions.TransactionValidateException;

import java.math.BigDecimal;
import java.util.Map;

import static br.com.zenon.domain.TransactionFieldKey.AMOUNT;

public class AmountValidation implements TransactionValidation {
    @Override
    public void validate(Map<TransactionFieldKey, Object> mapLine) throws TransactionValidateException {
        if (!mapLine.containsKey(AMOUNT)) {
            throw new IndexException("Amount Index not found");
        }
        Object value = mapLine.get(AMOUNT);
        if (value == null || new BigDecimal(value.toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountException("Invalid " + value + " for amount");
        }
    }
}
