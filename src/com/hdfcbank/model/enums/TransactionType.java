package com.hdfcbank.model.enums;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
