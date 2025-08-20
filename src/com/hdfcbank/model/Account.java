package com.hdfcbank.model;

import com.hdfcbank.exception.InsufficientBalanceException;

import java.util.Objects;

public abstract class Account {
    protected String accountId;
    protected Customer customer;
    protected double balance;

    public Account(String accountId, Customer customer, double balance) {
        this.accountId = accountId;
        this.customer = customer;
        this.balance = balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public abstract void withdraw(double amount) throws InsufficientBalanceException;
    public abstract double calculateInterest();

    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }

    @Override
    public String toString() {
        return accountId + " [" + customer.getName() + "] Balance=" + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
