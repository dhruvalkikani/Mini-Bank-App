package com.hdfcbank.model;

import com.hdfcbank.model.enums.TransactionType;

import java.time.LocalDateTime;

public class Transaction {
    private String txnId;
    private String accountId;
    private TransactionType type;
    private double amount;
    private LocalDateTime date;

    public Transaction(String txnId, String accId, TransactionType type, double amount) {
        this.txnId = txnId;
        this.accountId = accId;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }
    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() { return type; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return txnId + " | " + type + " | " + amount + " | " + date;
    }
}
