package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.IndexException;
import br.com.zenon.exceptions.StepException;
import br.com.zenon.exceptions.TransactionValidateException;

import java.util.Map;

import static br.com.zenon.domain.TransactionFieldKey.*;

public class StepValidation implements TransactionValidation {
    @Override
    public void validate(Map<TransactionFieldKey, Object> mapLine) throws TransactionValidateException {
        if (!mapLine.containsKey(STEP)) {
            throw new IndexException("Step Index not found");
        }
        Object value = mapLine.get(STEP);
        if (value == null || Long.parseLong(value.toString()) < 0) {
            throw new StepException("Invalid " + value + " for step");
        }
    }
}
