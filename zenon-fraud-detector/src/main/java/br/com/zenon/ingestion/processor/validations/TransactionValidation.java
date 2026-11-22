package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.TransactionValidateException;

import java.util.Map;

@FunctionalInterface
public interface TransactionValidation {

    void validate(Map<TransactionFieldKey,Object> mapLine) throws TransactionValidateException;
}
