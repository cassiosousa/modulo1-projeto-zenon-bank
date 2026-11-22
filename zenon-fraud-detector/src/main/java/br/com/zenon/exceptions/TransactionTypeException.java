package br.com.zenon.exceptions;

public class TransactionTypeException extends TransactionValidateException {
    public TransactionTypeException(String message) {
        super(message);
    }
}
