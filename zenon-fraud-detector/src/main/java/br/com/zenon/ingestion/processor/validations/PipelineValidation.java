package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.TransactionValidateException;

import java.util.Map;

public interface PipelineValidation {
    void execute(Map<TransactionFieldKey,Object> mapLine) throws TransactionValidateException;
}
