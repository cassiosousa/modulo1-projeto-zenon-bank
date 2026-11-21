package br.com.zenon.domain;

public enum TransactionStep {
    ONE(1), TWO(2);
    private int value;

    TransactionStep(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
