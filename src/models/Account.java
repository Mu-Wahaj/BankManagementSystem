package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Account implements Serializable {
    private String accountNumber;
    private String customerId;
    private String accountType;
    private double balance;
    private LocalDate openingDate;
    private double interestRate;
    private double minimumBalance;
    private String status;

        public Account(String accountNumber, String customerId, String accountType,
                   double initialDeposit) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.openingDate = LocalDate.now();
        setAccountParameters();
        this.status = "Active";
    }

    private void setAccountParameters() {
        switch (accountType) {
            case "Savings":
                interestRate = 5.0;
                minimumBalance = 1000.0;
                break;
            case "Current":
                interestRate = 1.0;                 minimumBalance = 5000.0;
                break;
            case "Fixed Deposit":
                interestRate = 8.0;
                minimumBalance = 10000.0;
                break;
            default:
                interestRate = 0.0;
                minimumBalance = 0.0;
        }
    }


    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
        setAccountParameters();
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(double minimumBalance) { this.minimumBalance = minimumBalance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

        public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + ". New Balance: " + balance);
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (balance - amount < minimumBalance) {
            System.out.println("Insufficient balance. Minimum balance required: " + minimumBalance);
            return false;
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount + ". New Balance: " + balance);
        return true;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }

    public double calculateInterest() {
        return balance * (interestRate / 100) / 12; // Monthly interest
    }

    @Override
    public String toString() {
        return accountNumber + "|" + customerId + "|" + accountType + "|" +
                balance + "|" + openingDate + "|" + interestRate + "|" +
                minimumBalance + "|" + status;
    }

    public static Account fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 8) return null;

        Account account = new Account(
                parts[0], parts[1], parts[2], Double.parseDouble(parts[3])
        );
        account.setOpeningDate(LocalDate.parse(parts[4]));
        account.setInterestRate(Double.parseDouble(parts[5]));
        account.setMinimumBalance(Double.parseDouble(parts[6]));
        account.setStatus(parts[7]);
        return account;
    }

    public void displayDetails() {
        System.out.println("\n=== ACCOUNT DETAILS ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Account Type: " + accountType);
        System.out.println("Balance: " + balance);
        System.out.println("Opening Date: " + openingDate);
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Minimum Balance: " + minimumBalance);
        System.out.println("Status: " + status);
    }
}