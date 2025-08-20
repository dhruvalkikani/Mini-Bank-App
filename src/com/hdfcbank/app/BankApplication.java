package com.hdfcbank.app;

import com.hdfcbank.exception.InsufficientBalanceException;
import com.hdfcbank.exception.InvalidAccountException;
import com.hdfcbank.model.*;
import com.hdfcbank.model.enums.AccountType;
import com.hdfcbank.service.BankService;
import com.hdfcbank.service.BankServiceImpl;
import com.hdfcbank.util.Validator;

import java.util.List;
import java.util.Scanner;

public class BankApplication {
    private static final Scanner sc = new Scanner(System.in);
    private static final BankServiceImpl bankService = new BankServiceImpl();

    public static void main(String[] args) {
        int choice;

        System.out.println("=== HDFC Mini Bank - Java Training Project ===");
        System.out.println("Welcome to the Banking Application!");

        do {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Register New Customer");
            System.out.println("2. Create Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Transaction History");
            System.out.println("7. Demo Mode (Complete Flow)");
            System.out.println("8. Simulate Concurrent Transfers");
            System.out.println("0. Exit");
            System.out.print("Enter option: ");

            choice = 0;
            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> registerCustomer();
                    case 2 -> createAccount();
                    case 3 -> deposit();
                    case 4 -> withdraw();
                    case 5 -> transfer();
                    case 6 -> showTransactions();
                    case 7 -> demoMode();
                    case 8 -> simulateConcurrentTransfers();
                    case 0 -> System.out.println("Thank you for banking with us!");
                    default -> System.out.println("Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("⚠ Error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    private static void registerCustomer() {
        System.out.println("\n=== Register New Customer ===");
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        if (!Validator.isValidEmail(email)) throw new IllegalArgumentException("Invalid Email!");

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();
        if (!Validator.isValidPhone(phone)) throw new IllegalArgumentException("Invalid Phone!");

        System.out.print("Enter DOB (yyyy-mm-dd): ");
        String dob = sc.nextLine();

        Customer c = bankService.registerCustomer(name, email, phone, dob);
        System.out.println("✓ Customer Registered: " + c);
    }

    private static void createAccount() {
        System.out.println("\n=== Create Account ===");
        System.out.print("Enter Customer ID: ");
        String custId = sc.nextLine();

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account (4.5% interest, min balance ₹1000)");
        System.out.println("2. Current Account (0% interest, no min balance)");
        int type = Integer.parseInt(sc.nextLine());
        AccountType accType = (type == 1) ? AccountType.SAVINGS : AccountType.CURRENT;

        System.out.print("Enter Initial Balance: ");
        double bal = Double.parseDouble(sc.nextLine());

        Account acc = bankService.createAccount(custId, accType, bal);
        System.out.println("✓ Account Created: " + acc);
        System.out.println("Interest Rate: " + (acc.calculateInterest()/acc.getBalance()*100) + "%");
    }

    private static void deposit() {
        System.out.println("\n=== Deposit Money ===");
        System.out.print("Enter Account ID: ");
        String accId = sc.nextLine();

        System.out.print("Enter Deposit Amount: ");
        double amt = Double.parseDouble(sc.nextLine());

        bankService.deposit(accId, amt);
        Account acc = bankService.getAccount(accId);
        System.out.println("✓ Deposit successful. New Balance: " + acc.getBalance());
    }

    private static void withdraw() throws InsufficientBalanceException, InvalidAccountException {
        System.out.println("\n=== Withdraw Money ===");
        System.out.print("Enter Account ID: ");
        String accId = sc.nextLine();

        System.out.print("Enter Withdraw Amount: ");
        double amt = Double.parseDouble(sc.nextLine());

        bankService.withdraw(accId, amt);
        Account acc = bankService.getAccount(accId);
        System.out.println("✓ Withdrawal successful. New Balance: " + acc.getBalance());
    }

    private static void transfer() throws InvalidAccountException, InsufficientBalanceException {
        System.out.println("\n=== Transfer Money ===");
        System.out.print("From Account ID: ");
        String from = sc.nextLine();

        System.out.print("To Account ID: ");
        String to = sc.nextLine();

        System.out.print("Amount: ");
        double amt = Double.parseDouble(sc.nextLine());

        bankService.transfer(from, to, amt);
        System.out.println("✓ Transfer completed successfully.");
        System.out.println("From Account Balance: " + bankService.getAccount(from).getBalance());
        System.out.println("To Account Balance: " + bankService.getAccount(to).getBalance());
    }

    private static void showTransactions() {
        System.out.println("\n=== Transaction History ===");
        System.out.print("Enter Account ID: ");
        String accId = sc.nextLine();

        List<Transaction> txns = bankService.getTransactions(accId);
        if (txns.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            System.out.println("Transactions for Account " + accId + ":");
            System.out.println("----------------------------------------");
            txns.forEach(System.out::println);
        }
    }

    private static void simulateConcurrentTransfers() {
        System.out.println("\n=== Simulate Concurrent Transfers ===");
        System.out.print("Enter From Account ID: ");
        String from = sc.nextLine();

        System.out.print("Enter To Account ID: ");
        String to = sc.nextLine();

        System.out.print("Enter Transfer Amount: ");
        double amt = Double.parseDouble(sc.nextLine());

        System.out.print("Enter Number of Concurrent Transfers: ");
        int numThreads = Integer.parseInt(sc.nextLine());

        bankService.simulateConcurrentTransfers(from, to, amt, numThreads);
    }

    private static void demoMode() {
        System.out.println("\n=== Demo Mode - Complete Banking Flow ===");
        System.out.println("Demonstrating all Java concepts...\n");

        try {
            // 1. Customer Registration
            System.out.println("1. Registering customers...");
            Customer c1 = bankService.registerCustomer("Rahul Sharma", "rahul@example.com", "9876543210", "1995-03-15");
            Customer c2 = bankService.registerCustomer("Priya Patel", "priya@example.com", "9123456780", "1992-11-25");
            Customer c3 = bankService.registerCustomer("Amit Kumar", "amit@example.com", "9988776655", "1990-07-10");
            System.out.println("✓ Customers registered successfully");

            // 2. Account Creation (Polymorphism)
            System.out.println("\n2. Creating accounts (demonstrating polymorphism)...");
            Account a1 = bankService.createAccount(c1.getCustomerId(), AccountType.SAVINGS, 5000);
            Account a2 = bankService.createAccount(c2.getCustomerId(), AccountType.CURRENT, 10000);
            Account a3 = bankService.createAccount(c3.getCustomerId(), AccountType.SAVINGS, 15000);
            System.out.println("✓ Accounts created successfully");

            // 3. Transaction Operations
            System.out.println("\n3. Performing transactions...");
            bankService.deposit(a1.getAccountId(), 2000);
            System.out.println("✓ Deposit successful");

            bankService.withdraw(a1.getAccountId(), 1500);
            System.out.println("✓ Withdrawal successful");

            bankService.transfer(a1.getAccountId(), a2.getAccountId(), 1000);
            System.out.println("✓ Transfer successful");

            // 4. Account Details (showing polymorphism)
            System.out.println("\n4. Account details (demonstrating polymorphism):");
            System.out.println("Savings Account: ₹" + String.format("%.2f", a1.getBalance()) +
                    " (Interest: ₹" + String.format("%.2f", a1.calculateInterest()) + ")");
            System.out.println("Current Account: ₹" + String.format("%.2f", a2.getBalance()) +
                    " (Interest: ₹" + String.format("%.2f", a2.calculateInterest()) + ")");

            // 5. Java 8 Streams demonstration
            System.out.println("\n5. Transaction history (using Java 8 Streams):");
            List<Transaction> a1Txns = bankService.getTransactions(a1.getAccountId());
            System.out.println("Account " + a1.getAccountId() + " transactions:");
            a1Txns.stream()
                    .forEach(t -> System.out.println("  " + t));

            // 6. Concurrent transfers demonstration
            System.out.println("\n6. Demonstrating concurrent transfers (thread safety):");
            bankService.simulateConcurrentTransfers(a3.getAccountId(), a1.getAccountId(), 500, 3);

            // 7. Final summary
            System.out.println("\n=== Demo completed successfully! ===");
            System.out.println("All Java concepts demonstrated:");
            System.out.println("✓ OOPs: Inheritance, Polymorphism, Encapsulation, Abstraction");
            System.out.println("✓ Collections: HashMap, ArrayList, proper equals/hashCode");
            System.out.println("✓ Java 8: Streams, Lambdas, Date/Time API");
            System.out.println("✓ Exception Handling: Custom exceptions and proper error handling");
            System.out.println("✓ Concurrency: Thread safety and concurrent operations");
            System.out.println("✓ Input Validation: Regex patterns and validation");

        } catch (Exception e) {
            System.out.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
