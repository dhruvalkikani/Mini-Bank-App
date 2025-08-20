package com.hdfcbank.model;

import com.hdfcbank.exception.InsufficientBalanceException;

public class SavingsAccount extends Account {
    private static final double MIN_BAL = 1000;
    private static final double INTEREST_RATE = 0.045;

    public SavingsAccount(String id, Customer cust, double bal) {
        super(id, cust, bal);
    }

    @Override
    public void withdraw(double amt) throws InsufficientBalanceException {
        if (balance - amt < MIN_BAL) throw new InsufficientBalanceException("Insufficient balance in Savings.");
        balance -= amt;
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}
