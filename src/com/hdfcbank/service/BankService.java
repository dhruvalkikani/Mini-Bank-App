package com.hdfcbank.service;

import com.hdfcbank.exception.InsufficientBalanceException;
import com.hdfcbank.exception.InvalidAccountException;
import com.hdfcbank.model.*;
import com.hdfcbank.model.enums.AccountType;
import java.util.List;

public interface BankService {
    Customer registerCustomer(String name, String email, String phone, String dob);
    Account createAccount(String customerId, AccountType type, double bal);
    void deposit(String accountId, double amt);
    void withdraw(String accountId, double amt) throws InsufficientBalanceException, InvalidAccountException;
    void transfer(String fromAcc, String toAcc, double amt) throws InsufficientBalanceException, InvalidAccountException;
    List<Transaction> getTransactions(String accId);
    Account getAccount(String accountId);
    Customer getCustomer(String customerId);
}
