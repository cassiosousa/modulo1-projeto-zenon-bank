package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.domain.TransactionType;
import br.com.zenon.exceptions.IndexException;
import br.com.zenon.exceptions.TransactionTypeException;
import br.com.zenon.exceptions.TransactionValidateException;

import java.util.Map;

import static br.com.zenon.domain.TransactionFieldKey.TYPE;

public class TypeValidation implements TransactionValidation {
    @Override
    public void validate(Map<TransactionFieldKey, Object> mapLine) throws TransactionValidateException {
        if (!mapLine.containsKey(TYPE)) {
            throw new IndexException("Type Index not found");
        }
        Object value = mapLine.get(TYPE);
        if (value == null) {
            throw new TransactionTypeException("Invalid " + value + " for type");
        }
        TransactionType.get(value.toString());
    }
}
