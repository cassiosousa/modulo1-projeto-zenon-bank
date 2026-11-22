package br.com.zenon.domain;

import br.com.zenon.exceptions.IndexException;

public enum TransactionFieldKey {
    STEP(0),
    TYPE(1),
    AMOUNT(2),
    CUSTOMER_ORIGIN_NAME(3),
    CUSTOMER_ORIGIN_OLD_BALANCE(4),
    CUSTOMER_ORIGIN_NEW_BALANCE(5),
    CUSTOMER_DESTINATION_NAME(6),
    CUSTOMER_DESTINATION_OLD_BALANCE(7),
    CUSTOMER_DESTINATION_NEW_BALANCE(8),
    IS_FRAUD(9),
    IS_FLAGGED_FRAUD(10);

    private int index;

    TransactionFieldKey(int index) {
        this.index = index;
    }

    public static TransactionFieldKey get(int index) throws IndexException {
        if (index < 0 || index > values().length) {
            throw new IndexException("TransactionField key withg index:" + index + " not found.");
        }
        return values()[index];
    }
}
