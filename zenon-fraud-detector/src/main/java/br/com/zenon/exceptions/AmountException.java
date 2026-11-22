package br.com.zenon.exceptions;

import br.com.zenon.ingestion.processor.validations.TransactionValidation;

public class AmountException extends TransactionValidateException {
    public AmountException(String message) {
        super(message);
    }
}
