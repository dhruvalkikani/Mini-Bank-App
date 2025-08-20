package com.hdfcbank.model;

import com.hdfcbank.exception.InsufficientBalanceException;

public class CurrentAccount extends Account {
    public CurrentAccount(String id, Customer cust, double bal) {
        super(id, cust, bal);
    }

    @Override
    public void withdraw(double amt) throws InsufficientBalanceException {
        if (balance < amt) throw new InsufficientBalanceException("Insufficient balance in Current Account.");
        balance -= amt;
    }

    @Override
    public double calculateInterest() {
        return 0; // No interest
    }
}
