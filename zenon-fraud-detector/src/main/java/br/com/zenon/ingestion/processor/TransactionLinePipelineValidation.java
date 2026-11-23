package br.com.zenon.ingestion.processor;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.TransactionValidateException;
import br.com.zenon.ingestion.processor.validations.*;

import java.util.List;
import java.util.Map;

public class TransactionLinePipelineValidation implements PipelineValidation {

    private final List<TransactionValidation> lineValidations = List.of(
            new StepValidation(),
            new TypeValidation(),
            new AmountValidation(),
            new CustomerOriginValidation(),
            new CustomerDestinationValidation()
    );

    @Override
    public void execute(Map<TransactionFieldKey, Object> mapLine) throws TransactionValidateException {
        for (TransactionValidation item : lineValidations) {
            item.validate(mapLine);
        }
    }
}
