package com.hdfcbank.service;

import com.hdfcbank.exception.InsufficientBalanceException;
import com.hdfcbank.exception.InvalidAccountException;
import com.hdfcbank.model.*;
import com.hdfcbank.model.enums.AccountType;
import com.hdfcbank.model.enums.TransactionType;
import com.hdfcbank.util.IDGenerator;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public Customer registerCustomer(String name, String email, String phone, String dob) {
        String id = IDGenerator.generateCustomerId();
        Customer c = new Customer(id, name, email, phone, LocalDate.parse(dob));
        customers.put(id, c);
        return c;
    }

    @Override
    public Account createAccount(String customerId, AccountType type, double bal) {
        Customer customer = customers.get(customerId);
        if (customer == null) throw new IllegalArgumentException("Customer not found");

        String accId = IDGenerator.generateAccountId();
        Account acc = (type == AccountType.SAVINGS) ? new SavingsAccount(accId, customer, bal)
                : new CurrentAccount(accId, customer, bal);
        accounts.put(accId, acc);
        return acc;
    }

    @Override
    public void deposit(String accountId, double amt) {
        Account acc = accounts.get(accountId);
        if (acc == null) throw new IllegalArgumentException("Invalid Account");
        acc.deposit(amt);
        transactions.add(new Transaction(IDGenerator.generateTxnId(), acc.getAccountId(), TransactionType.DEPOSIT, amt));
    }

    @Override
    public void withdraw(String accountId, double amt) throws InsufficientBalanceException, InvalidAccountException {
        Account acc = accounts.get(accountId);
        if (acc == null) throw new InvalidAccountException("Invalid Account");
        acc.withdraw(amt);
        transactions.add(new Transaction(IDGenerator.generateTxnId(), acc.getAccountId(), TransactionType.WITHDRAW, amt));
    }

    @Override
    public void transfer(String fromAcc, String toAcc, double amt) throws InsufficientBalanceException, InvalidAccountException {
        Account src = accounts.get(fromAcc);
        Account dest = accounts.get(toAcc);
        if (src == null || dest == null) throw new InvalidAccountException("Invalid Account(s)");

        // Thread-safe transfer
        synchronized(src) {
            synchronized(dest) {
                src.withdraw(amt);
                dest.deposit(amt);
            }
        }
        transactions.add(new Transaction(IDGenerator.generateTxnId(), src.getAccountId(), TransactionType.TRANSFER, amt));
    }

    @Override
    public List<Transaction> getTransactions(String accId) {
        return transactions.stream()
                .filter(t -> t.getAccountId().equals(accId))
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    // Method for concurrent transfers demonstration
    public void simulateConcurrentTransfers(String fromAcc, String toAcc, double amt, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        System.out.println("=== Starting Concurrent Transfer Simulation ===");
        System.out.println("Initial Balance - From: " + getAccount(fromAcc).getBalance() +
                ", To: " + getAccount(toAcc).getBalance());

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int transferNum = i + 1;
            Future<String> future = executor.submit(() -> {
                try {
                    transfer(fromAcc, toAcc, amt);
                    return "Transfer " + transferNum + " successful";
                } catch (Exception e) {
                    return "Transfer " + transferNum + " failed: " + e.getMessage();
                }
            });
            futures.add(future);
        }

        futures.forEach(future -> {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                System.out.println("Transfer failed: " + e.getMessage());
            }
        });

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Balance - From: " + getAccount(fromAcc).getBalance() +
                ", To: " + getAccount(toAcc).getBalance());
    }
}
