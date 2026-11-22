package br.com.zenon.exceptions;

public class StepException extends TransactionValidateException {
    public StepException(String message) {
        super(message);
    }
}