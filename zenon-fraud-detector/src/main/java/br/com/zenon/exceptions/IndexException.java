package br.com.zenon.exceptions;

public class IndexException extends TransactionValidateException {
    public IndexException(String message) {
        super(message);
    }
}
